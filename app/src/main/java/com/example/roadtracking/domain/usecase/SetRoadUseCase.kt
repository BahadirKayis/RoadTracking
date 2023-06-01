package com.example.roadtracking.domain.usecase

import com.example.roadtracking.common.extensions.dateToLongConvert
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.domain.repository.RoadRepository
import javax.inject.Inject

class SetRoadUseCase @Inject constructor(private val repo: RoadRepository) {
    suspend operator fun invoke(road: RoadUI) = repo.setRoad(road)
}