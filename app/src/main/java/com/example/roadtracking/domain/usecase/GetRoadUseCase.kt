package com.example.roadtracking.domain.usecase

import com.example.roadtracking.domain.repository.RoadRepository
import javax.inject.Inject

class GetRoadUseCase @Inject constructor(private val repo: RoadRepository) {
    operator fun invoke() = repo.getRoad()
}