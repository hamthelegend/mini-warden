package com.thebrownfoxx.novel

class PromptContentScope<K : Any> {
    private val lines = mutableListOf<PromptLine>()
    private val actions = mutableListOf<Action<K>>()

    fun Character.says(vararg texts: String) {
        val promptLines = texts.map { PromptLine(speaker = this, text = it) }
        lines.addAll(promptLines)
    }

    fun choice(
        text: String,
        actionWithPromptKey: () -> K,
    ) {
        actions.add(Choice(text, actionWithPromptKey))
    }

    fun redirect(actionWithPromptKey: () -> K) {
        actions.add(Redirect(actionWithPromptKey))
    }

    internal fun toPromptContent(): PromptContent<K> {
        val redirect = actions.filterIsInstance<Redirect<K>>().firstOrNull()
        return when {
            actions.isEmpty() -> TerminalPromptContent(lines)
            redirect == null -> ChoicePromptContent(lines, actions.filterIsInstance<Choice<K>>())
            else -> RedirectPromptContent(lines, redirect)
        }
    }
}

fun <K : Any> prompt(block: PromptContentScope<K>.() -> Unit) = Prompt {
    PromptContentScope<K>().apply(block).toPromptContent()
}