package novel

import novel.PromptKey.*

val Narrator = Character("Narrator")
val Steve = Character("Steve")
val Alex = Character("Alex")

data class Item(val name: String)

class GameState {
    val shovel = Item("Shovel")

    private val _inventory = mutableListOf<Item>()
    val inventory: List<Item> = _inventory

    private val prompts: Map<PromptKey, Prompt<PromptKey>> = mapOf(
        SteveHorny to {
            Steve.says(
                "Man, I'm horny.",
                "What should I do?",
            ).withChoices(
                Choice("Masturbate") { SteveJerksOff },
                Choice("Fuck Alex") { SteveFucksAlex },
            )
        },
        SteveJerksOff to {
            Narrator.says("Steve is no longer horny. You win!").withNoChoices()
        },
        SteveFucksAlex to {
            promptLines(
                Alex.says("$Steve what are you doing?"),
                Narrator.says("$Alex died from $Steve's horse cock."),
            ).actionWithPromptKey { StevePanics }
        },
        StevePanics to {
            Steve.says("[Whimpers] What do I do? What do I do?").withChoices(
                Choice("Bury $Alex") { SteveBuriesAlex },
                Choice("Open closet") { SteveOpensCloset },
            )
        },
        SteveOpensCloset to {
            when (shovel) {
                !in inventory -> promptLines(
                    Steve.says("Nice! A shovel!"),
                    Narrator.says("Steve got a shovel."),
                ).actionWithPromptKey {
                    _inventory.add(shovel)
                    StevePanics
                }

                else -> Steve.says("I don't think there's any more for me here.").actionWithPromptKey { StevePanics }
            }
        },
        SteveBuriesAlex to {
            when (shovel) {
                !in inventory -> Steve.says("I need a shovel for that.").actionWithPromptKey { StevePanics }
                else -> Narrator.says("You buried Alex. You win!").withNoChoices()
            }
        }
    )

    val novel = Novel(prompts, firstPromptKey = SteveHorny)
}

fun main() {
    val gameState = GameState()
    while (true) {
        println("--------------------------------------------------")
        println("Inventory: ${gameState.inventory.map { it.name }}")
        val prompt = gameState.novel.currentPrompt()
        for (line in prompt.lines) {
            println(
                when (line.speaker) {
                    Narrator -> line.text
                    else ->"${line.speaker}: ${line.text}"
                }
            )
        }
        if (prompt.choices.isEmpty()) {
            break
        }
        val choice = if (prompt.choices.size == 1) {
            val onlyChoice = prompt.choices.first()
            print("[Enter] ${onlyChoice.text}")
            readln()
            onlyChoice
        } else {
            var chosenChoice: Choice<PromptKey>? = null
            while (chosenChoice == null) {
                for ((index, choice) in prompt.choices.withIndex()) {
                    println("[${index + 1}] - ${choice.text}")
                }
                print("Enter the number of your choice: ")
                val chosenIndex = readln().toIntOrNull()?.minus(1) ?: -1
                chosenChoice = prompt.choices.getOrNull(chosenIndex)
            }
            chosenChoice
        }
        gameState.novel.choose(choice)
    }
}