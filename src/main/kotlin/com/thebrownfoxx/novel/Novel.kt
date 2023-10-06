@file:Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")

package com.thebrownfoxx.novel

class IllegalPromptKeyException(val key: Any) :
        IllegalArgumentException("There is no prompt with the key $key.")

class Novel<K : Any>(
    prompts: Map<K, Prompt<K>>,
    firstPromptKey: K,
) {
    val prompts = HashMap(prompts)

    var currentPrompt = prompts[firstPromptKey] ?: throw IllegalPromptKeyException(firstPromptKey)
        private set

    fun doAction(action: Action<K>) {
        val key = action.actionWithPromptKey()
        currentPrompt = prompts[key] ?: throw IllegalPromptKeyException(key)
    }
}