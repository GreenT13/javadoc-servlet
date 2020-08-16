plugins {
    id("com.moowork.node") version "1.3.1"
}

// Serve documentation.
tasks.register<com.moowork.gradle.node.npm.NpmTask>("serve") {
    setArgs(listOf("run", "serve"))
}

// Whenever running the serve command, NodeJS server keeps running. To avoid this, we add a kill command.
// THe command will say it failed, but it actually didn't.
tasks.register<com.moowork.gradle.node.npm.NpmTask>("kill") {
    setArgs(listOf("run", "kill"))
}
