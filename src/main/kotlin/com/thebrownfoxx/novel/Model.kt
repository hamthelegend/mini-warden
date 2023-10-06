package com.thebrownfoxx.novel

data class Character(val name: String) {
    override fun toString() = name
}

sealed class Action<K : Any> {
    abstract val actionWithPromptKey: () -> K
}

data class Choice<K : Any>(
    val text: String,
    override val actionWithPromptKey: () -> K,
) : Action<K>()

data class Redirect<K : Any>(
    override val actionWithPromptKey: () -> K,
) : Action<K>()

data class PromptLine(
    val speaker: Character,
    val text: String,
)

sealed class PromptContent<K : Any> {
    abstract val lines: List<PromptLine>
}

data class ChoicePromptContent<K : Any>(
    override val lines: List<PromptLine>,
    val choices: List<Choice<K>>,
) : PromptContent<K>()

data class RedirectPromptContent<K : Any>(
    override val lines: List<PromptLine>,
    val redirect: Redirect<K>,
) : PromptContent<K>()

data class TerminalPromptContent<K : Any>(
    override val lines: List<PromptLine>,
) : PromptContent<K>()

data class Prompt<K : Any>(val content: () -> PromptContent<K>)