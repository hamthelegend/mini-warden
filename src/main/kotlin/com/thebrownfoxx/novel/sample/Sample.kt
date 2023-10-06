package com.thebrownfoxx.novel.sample

import com.thebrownfoxx.novel.Action
import com.thebrownfoxx.novel.Character
import com.thebrownfoxx.novel.Choice
import com.thebrownfoxx.novel.ChoicePromptContent
import com.thebrownfoxx.novel.Novel
import com.thebrownfoxx.novel.Prompt
import com.thebrownfoxx.novel.RedirectPromptContent
import com.thebrownfoxx.novel.TerminalPromptContent
import com.thebrownfoxx.novel.prompt
import com.thebrownfoxx.novel.sample.PromptKey.*

val Narrator = Character("Narrator")
val Steve = Character("Steve")
val Alex = Character("Alex")

data class Item(val name: String)

class GameState {
    private val shovel = Item("Shovel")

    private val _inventory = mutableListOf<Item>()
    val inventory: List<Item> = _inventory

    private val prompts: Map<PromptKey, Prompt<PromptKey>> = mapOf(
        SteveHorny to prompt {
            Steve.says(
                "Man, I'm horny.",
                "What should I do?",
            )
            choice("Masturbate") { SteveJerksOff }
            choice("Fuck Alex") { SteveFucksAlex }
        },
        SteveJerksOff to prompt {
            Narrator.says("Steve is no longer horny. You win!")
        },
        SteveFucksAlex to prompt {
            Alex.says("$Steve what are you doing?")
            Narrator.says("$Alex died from $Steve's horse cock.")
            redirect { StevePanics }
        },
        StevePanics to prompt {
            Steve.says("[Whimpers] What do I do? What do I do?")
            choice("Bury $Alex") { SteveBuriesAlex }
            choice("Open closet") { SteveOpensCloset }
        },
        SteveOpensCloset to prompt {
            if (shovel !in inventory) {
                Steve.says("Nice! A shovel!")
                Narrator.says("Steve got a shovel.")
                redirect {
                    _inventory.add(shovel)
                    StevePanics
                }
            } else {
                Steve.says("I don't think there's any more for me here.")
                redirect { StevePanics }
            }
        },
        SteveBuriesAlex to prompt {
            if (shovel !in inventory) {
                Steve.says("I need a shovel for that.")
                redirect { StevePanics }
            } else {
                Narrator.says("You buried Alex. You win!")
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
        val prompt = gameState.novel.currentPrompt.content()
        for (line in prompt.lines) {
            println(
                when (line.speaker) {
                    Narrator -> line.text
                    else -> "${line.speaker}: ${line.text}"
                }
            )
        }

        val action: Action<PromptKey>

        when (prompt) {
            is TerminalPromptContent -> break

            is RedirectPromptContent -> {
                print("[Enter]")
                readln()
                action = prompt.redirect
            }

            is ChoicePromptContent -> {
                var chosenChoice: Choice<PromptKey>? = null
                while (chosenChoice == null) {
                    for ((index, choice) in prompt.choices.withIndex()) {
                        println("[${index + 1}] - ${choice.text}")
                    }
                    print("Enter the number of your choice: ")
                    val chosenIndex = readln().toIntOrNull()?.minus(1) ?: -1
                    chosenChoice = prompt.choices.getOrNull(chosenIndex)
                }
                action = chosenChoice
            }
        }
        gameState.novel.doAction(action)
    }
}