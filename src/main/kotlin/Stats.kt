import kotlin.math.min

enum class StatType {
    Strength,
    Endurance,
    Agility,
    Sanity,
}

data class Stat(
    val statType: StatType,
    val value: Int,
) {
    override fun toString() = "${statType.name.first()} $value"
}

data class Stats(
    val roll: Int,
    val stats: List<Stat>
) {
    companion object {
        const val MinRoll = 0
        const val MaxRoll = 200
        const val BaseStat = 20
        const val MaxStat = 100

        fun roll(): Stats {
            val roll = (MinRoll..MaxRoll).random()
            var additionalStatistics = roll

            val stats = StatType.entries
                .map { Stat(statType = it, value = BaseStat) }
                .toMutableList()

            while (additionalStatistics > 0) {
                val (currentStatIndex, currentStat) = stats.withIndex().toList().random()

                val maxStatRoll = min(MaxStat - currentStat.value, additionalStatistics)
                val statRoll = (0..maxStatRoll).random()
                val newStatValue = currentStat.value + statRoll

                stats[currentStatIndex] = currentStat.copy(value = newStatValue)
                additionalStatistics -= statRoll
            }

            return Stats(
                roll = roll,
                stats = stats,
            )
        }
    }

    override fun toString(): String {
        var string = "R $roll\t| "
        for (stat in stats) {
            string += "$stat\t"
        }
        return string
    }
}

fun main() {
    repeat(20) {
        println(Stats.roll())
    }
}