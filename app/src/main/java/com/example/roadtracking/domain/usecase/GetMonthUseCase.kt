package com.example.roadtracking.domain.usecase

import com.example.roadtracking.domain.repository.RoadRepository
import javax.inject.Inject

class GetMonthUseCase @Inject constructor(private val repo: RoadRepository) {
    operator fun invoke(month: Int) = repo.sendMonth(month)
}