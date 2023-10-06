@file:Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")

package novel

class IllegalPromptKeyException(val key: Any) :
        IllegalArgumentException("There is no prompt with the key $key.")

class Novel<K : Any>(
    prompts: Map<K, Prompt<K>>,
    firstPromptKey: K,
) {
    val prompts = HashMap(prompts)

    var currentPrompt = prompts[firstPromptKey] ?: throw IllegalPromptKeyException(firstPromptKey)
        private set

    fun choose(choice: Choice<K>) {
        val key = choice.actionWithPromptKey()
        currentPrompt = prompts[key] ?: throw IllegalPromptKeyException(key)
    }
}