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