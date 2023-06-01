package com.example.roadtracking.domain.usecase


import com.example.roadtracking.domain.repository.RoadRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repo: RoadRepository) {
    operator fun invoke(company: String) = repo.searchCompany(company)
}