# Contributing to FlowTab

First off, thank you for considering contributing to FlowTab! It's people like you that make FlowTab such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps to reproduce the problem**
* **Provide specific examples** to demonstrate the steps
* **Describe the behavior you observed** and what you expected to see
* **Include screenshots or animated GIFs** if possible
* **Include your environment details**: Kotlin version, Compose version, device/emulator, OS version

**Bug Report Template:**
```markdown
**Description:**
A clear and concise description of the bug.

**To Reproduce:**
1. Go to '...'
2. Click on '...'
3. See error

**Expected behavior:**
What you expected to happen.

**Screenshots:**
If applicable, add screenshots.

**Environment:**
- FlowTab version: [e.g., 1.0.0]
- Kotlin version: [e.g., 2.2.21]
- Compose Multiplatform version: [e.g., 1.9.3]
- Device: [e.g., Pixel 6, iOS Simulator]
- OS: [e.g., Android 14, iOS 17]
```

### Suggesting Features

Feature suggestions are tracked as GitHub issues. When creating a feature suggestion, please include:

* **Use a clear and descriptive title**
* **Provide a detailed description** of the suggested feature
* **Explain why this feature would be useful** to most FlowTab users
* **Provide examples** of how the feature would be used
* **List any alternatives** you've considered

### Pull Requests

1. **Fork the repository** and create your branch from `main`
2. **Make your changes** following our coding standards
3. **Add tests** if you've added functionality
4. **Ensure the test suite passes**
5. **Update documentation** if needed
6. **Write a clear commit message**

#### Coding Standards

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions small and focused
- Maintain the existing code style
- Ensure backward compatibility when possible

#### Commit Messages

Write clear and meaningful commit messages:

```
feat: Add support for custom badge colors
^--^  ^---------------------------^
|     |
|     +-> Summary in present tense
|
+-------> Type: feat, fix, docs, style, refactor, test, chore
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only changes
- `style`: Code style changes (formatting, missing semicolons, etc.)
- `refactor`: Code refactoring without changing functionality
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Development Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Alims-Repo/flow-tab-cmp.git
   cd flow-tab-cmp
   ```

2. **Open in your IDE:**
    - Android Studio (recommended)
    - IntelliJ IDEA
    - Fleet

3. **Build the project:**
   ```bash
   ./gradlew build
   ```

4. **Run tests:**
   ```bash
   ./gradlew test
   ```

### Project Structure

```
flow-tab-cmp/
├── src/
│   ├── commonMain/          # Shared Kotlin code
│   │   └── kotlin/
│   │       └── com/alim/flow_tab/
│   │           ├── BottomNavigation.kt
│   │           ├── domain/
│   │           │   └── model/
│   │           ├── ui/
│   │           │   ├── component/
│   │           │   ├── container/
│   │           │   ├── extension/
│   │           │   └── section/
│   │           └── util/
│   ├── androidMain/         # Android-specific code
│   ├── iosMain/             # iOS-specific code
│   ├── commonTest/          # Shared tests
│   └── androidDeviceTest/   # Android instrumented tests
└── build.gradle.kts
```

### Testing

- Write unit tests for new functionality
- Ensure all existing tests pass
- Test on both Android and iOS when possible
- Test with different configurations (blur enabled/disabled, various heights, etc.)

### Documentation

- Update README.md if you change functionality
- Add KDoc comments to public APIs
- Update code samples if behavior changes
- Keep documentation concise and clear

## Style Guide

### Kotlin Style

Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
fun calculateItemWidth(
    containerWidth: Dp,
    itemCount: Int
): Dp {
    return if (itemCount > 0) {
        containerWidth / itemCount
    } else {
        0.dp
    }
}

// Bad
fun calculateItemWidth(containerWidth:Dp,itemCount:Int):Dp{
    return if(itemCount>0) containerWidth/itemCount else 0.dp
}
```

### Compose Best Practices

```kotlin
// Good - Stable, remembered state
@Composable
fun MyComponent() {
    val items = remember {
        listOf(
            NavItem(id = "home", label = "Home", icon = Icons.Default.Home)
        )
    }
}

// Bad - Recreated every composition
@Composable
fun MyComponent() {
    val items = listOf(
        NavItem(id = "home", label = "Home", icon = Icons.Default.Home)
    )
}
```

## Questions?

Feel free to open an issue with the `question` label or start a discussion in the [Discussions](https://github.com/Alims-Repo/flow-tab-cmp/discussions) section.

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.

---

Thank you for contributing to FlowTab! 🎉