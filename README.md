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

## [Day 3](https://adventofcode.com/2023/day/3)

In the first part, for each line I found all the numbers with a regex.
To check if a number is a part number, the current line and the lines around
must be searched for symbols within the indices of the found number plus 1 char before and after,
so that adjacent symbols can also be found on the left, on the right and diagonally.

For the second part, I decided to store all the part numbers adjacent to each gear
in `Map<Int, MutableList<Long>>`. The key is the gear global index in the input.
So, each number is checked for being adjacent not to any symbol but to a gear (`*`),
and if it is, the number is stored.
To get the answer once all the numbers are processed, I took only the gears
which are adjacent to 2 numbers and summed their products.

## [Day 4](https://adventofcode.com/2023/day/4)

The first part is straightforward. Since number of points for card is a power of 2,
I used bit shifting to get the required value.

For the second part, I stored card counts by card index, which at the start are all `1`.
Then, when processing cards one by one, I increased the count of each won card by `1`
multiplied the count of the card being processed. Sum of counts at the end is the answer.

## [Day 5](https://adventofcode.com/2023/day/5)

While solving the first part, I spent most of the time parsing the input. Created a class for mapping,
added a few helper functions, then the answer became easy to calculate.

For the second part, I tried parallelized brute-force which gave the answer in around 2 minutes.
I also had an alternative idea – to run mapping in reverse checking what is the min achievable location
from 0 to 1,000,000,000. Since the answer is quite small (mine was 9,622,622), this could have worked too.

Apparently the fast solution for the second part is to overlay map ranges with the value ranges
so you end up with more ranges. Once it is done for all the maps, one after another,
the answer is the minimal range start. I'm glad brute-force worked, felt sick thinking about overlaying
ranges.

## [Day 6](https://adventofcode.com/2023/day/6)

Perhaps this day can be solved mathematically, but a simple loop counting the winning boat charge durations
worked for both parts.

[aoc]: https://adventofcode.com

[github]: https://github.com/radiokot

[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues

[kotlin]: https://kotlinlang.org

[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up

[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
