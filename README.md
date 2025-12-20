# Advent of Code 2023 in Kotlin

Welcome to the [Advent of Code][aoc] Kotlin project created by [Radiokot][github] using
the [Advent of Code Kotlin Template][template] delivered by JetBrains.

## [Day 1](https://adventofcode.com/2023/day/1)

For the first part, it's takes just `line.first(Char::isDigit)` and `line.last(Char::isDigit)`
to identify the digits.

For the second part, I tried regex, but it didn't work for the last digit.
Since regex runs from left to right, when facing things like `twoone`
it finds `two` instead of `one`. I replaced regex with `startsWith` and `endsWith` checks in a loop
which did the thing.

## [Day 2](https://adventofcode.com/2023/day/2)

The first part is solved by checking each game for having all the sets having all the entries not exceeding
maximum cube count for the entry color.

In the second part, I determined maximum cube count by color for each game
by `fold`ing sets (`Map<String, Int>`, count by color) into a `MutableMap<String, Int>`.
The current set entry count is put to the result map if it is bigger than the one already known for the entry color.

[aoc]: https://adventofcode.com

[github]: https://github.com/radiokot

[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues

[kotlin]: https://kotlinlang.org

[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up

[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
