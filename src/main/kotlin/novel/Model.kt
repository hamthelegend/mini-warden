package novel

data class Character(val name: String) {
    override fun toString() = name
}

data class Choice<K: Any>(
    val text: String,
    val actionWithPromptKey: () -> K,
)

data class PromptLine(
    val speaker: Character,
    val text: String,
)

data class PromptContent<K : Any>(
    val lines: List<PromptLine>,
    val choices: List<Choice<K>>,
)

typealias Prompt<K> = () -> PromptContent<K>

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