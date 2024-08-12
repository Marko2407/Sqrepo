package com.mvukosav.sqrepo.domain.usecases

import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_NETWORK_ERROR
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_NOT_FOUND
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_UNKNOWN_ERROR
import com.mvukosav.sqrepo.common.Resource
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.repository.SquareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRepositoryUseCase @Inject constructor(
    private val repository: SquareRepository,
) {

    operator fun invoke(id: Int): Flow<Resource<SquareRepo?>> = flow {
        try {
            emit(Resource.Loading())
            val repository = repository.getRepository(id)?.toDomain()
            if (repository != null) {
                emit(Resource.Success(repository))
            } else {
                emit(Resource.Error(ERROR_MESSAGE_NOT_FOUND))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: ERROR_MESSAGE_UNKNOWN_ERROR))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: ERROR_MESSAGE_NETWORK_ERROR))
        }
    }
}
