package novel

fun Character.says(vararg lines: String) =
    lines.map { PromptLine(this, it) }

fun promptLines(vararg promptLines: List<PromptLine>) =
    promptLines.flatMap { it }

fun <K : Any> List<PromptLine>.withChoices(vararg choices: Choice<K>) =
    PromptContent(
        lines = this,
        choices = choices.toList(),
    )

fun <K : Any> List<PromptLine>.withNoChoices() = withChoices<K>()

fun <K : Any> List<PromptLine>.actionWithPromptKey(promptKey: () -> K) =
    withChoices(Choice("", promptKey))