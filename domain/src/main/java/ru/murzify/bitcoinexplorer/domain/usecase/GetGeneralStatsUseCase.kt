package ru.murzify.bitcoinexplorer.domain.usecase

import ru.murzify.bitcoinexplorer.domain.repository.StatsRepository

class GetGeneralStatsUseCase(private val repo: StatsRepository) {
    suspend fun execute() = repo.getStats("bitcoin")
}
