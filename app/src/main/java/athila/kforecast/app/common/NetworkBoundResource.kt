/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package athila.kforecast.app.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import athila.kforecast.app.api.ApiEmptyResponse
import athila.kforecast.app.api.ApiErrorResponse
import athila.kforecast.app.api.ApiResponse
import athila.kforecast.app.api.ApiSuccessResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor() {

  private val result = MediatorLiveData<Resource<ResultType>>()

  protected open val coroutineScope: CoroutineScope = GlobalScope

  init {
    result.value = Resource.loading(null)
    @Suppress("LeakingThis")
    val dbSource = loadFromDb()
    result.addSource(dbSource) { data ->
      result.removeSource(dbSource)
      if (shouldFetch(data)) {
        fetchFromNetwork(dbSource)
      } else {
        result.addSource(dbSource) { newData ->
          setValue(Resource.success(newData))
        }
      }
    }
  }

  @MainThread
  private fun setValue(newValue: Resource<ResultType>) {
    if (result.value != newValue) {
      result.value = newValue
    }
  }

  private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
    val apiResponse = createCall()
    // we re-attach dbSource as a new source, it will dispatch its latest value quickly
    result.addSource(dbSource) { newData ->
      setValue(Resource.loading(newData))
    }
    result.addSource(apiResponse) { response ->
      result.removeSource(apiResponse)
      result.removeSource(dbSource)
      when (response) {
        is ApiSuccessResponse -> {
          coroutineScope.launch(AppExecutors.bgThread) {
            try {
              // do not save the results if the coroutine was cancelled
              if (coroutineContext[Job]?.isActive == true) {
                saveCallResult(processResponse(response))
              }
              withContext(AppExecutors.mainThread) {
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(loadFromDb()) { newData ->
                  setValue(Resource.success(newData))
                }
              }
            } catch (exception: CancellationException) {
              // TODO: remove this code duplicity
              result.addSource(loadFromDb()) { newData ->
                setValue(Resource.success(newData))
              }
            }
          }
        }
        is ApiEmptyResponse -> {
          coroutineScope.launch(AppExecutors.mainThread) {
            // reload from disk whatever we had
            result.addSource(loadFromDb()) { newData ->
              setValue(Resource.success(newData))
            }
          }
        }
        is ApiErrorResponse -> {
          onFetchFailed()
          result.addSource(dbSource) { newData ->
            setValue(Resource.error(response.error, newData))
          }
        }
      }
    }
  }

  protected open fun onFetchFailed() {}

  fun asLiveData() = result as LiveData<Resource<ResultType>>

  @WorkerThread
  protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

  @WorkerThread
  protected abstract fun saveCallResult(item: RequestType)

  @MainThread
  protected abstract fun shouldFetch(data: ResultType?): Boolean

  @MainThread
  protected abstract fun loadFromDb(): LiveData<ResultType>

  @MainThread
  protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
