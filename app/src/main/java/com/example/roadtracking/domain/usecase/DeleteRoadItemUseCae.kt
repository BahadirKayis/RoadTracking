package com.example.roadtracking.domain.usecase

import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.domain.repository.RoadRepository
import javax.inject.Inject

class DeleteRoadItemUseCae @Inject constructor(private val repo: RoadRepository) {
    suspend operator fun invoke(roadUI: RoadUI) = repo.deleteRoadItem(roadUI)
}