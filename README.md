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

## [Day 7](https://adventofcode.com/2023/day/7)

When solving this day, I did think hard on hand types and took some time for debugging, as I don't play cards.
It was *absolutely necessary* to create an enum for types, with Javadoc, so I didn't have to Alt-Tab
each time I forgot what a full house was.

## [Day 8](https://adventofcode.com/2023/day/8)

For the first part, I just implemented what the problem described –
a loop traversing `Map<String, Pair<String, String>>` while counting steps.

For the second part, I initially extended the loop to follow multiple path at once,
but it didn't seem to end. Then I thought that perhaps what must be found for each start node
is the number of steps until the path is looped, yet the loop with this check didn't seem to end either
(or maybe I implemented it wrong). Then I tried finding just step count to `Z` for each start node –
the values were reasonable. Had a gut feeling that the answer had something to do with the least common multiple.
To my surprise, it not just had – LCM of all the step counts to `Z` is the answer to the part 2.
I don't understand why.

## [Day 9](https://adventofcode.com/2023/day/9)

Interesting algorithm. Is this how value prediction really works?

For the first part, I implemented iterative calculation of lines, pushing each line last value to a stack.
Once done, to get the result, the stack of last values must be summed in this Fibonacci-like fashion:
```kotlin
lastValues.reduce { sumOfPrevious, value -> value + sumOfPrevious }
```

I didn't get what the catch was in the second part – I just saved each line first value instead,
and changed the reducing function:
```kotlin
firstValues.reduce { differenceOfPrevious, value -> value - differenceOfPrevious }
```

## [Day 10](https://adventofcode.com/2023/day/10)

Did write a ton of code for this day.

For the first part, implemented `PipeTraveler` class which follows the pipe and counts steps.
For some reason I thought first part needed the coordinate of the furthest point, ended up spending
extra time on what wasn't needed.

For the second part, I used my traveler to trace the perimeter of the pipe.
The rest drove me crazy – I tried ray casting but didn't manage to solve the edge case
when the ray goes right through an edge. After giving up on ray casting, decided to flood the outside
combined with the 3x map scale trick. Albeit tedious, transforming each cell into 3x3 makes the flood flow
between parallel pipes casually – for example, for `|L`, instead of 2 occupied cells a computer sees 9
```
.#..#.
.#..#.
.#..##
```
with very obvious empty space between pipes. What's left after the flood is to count the insideness,
which is done in a loop starting from `1` with the step of `3`, like if the map was scaled back.

## [Day 11](https://adventofcode.com/2023/day/11)

First, I identified indices of rows and columns that need expansion.
Then, I also obtained all the galaxies positions.

For the first part, before calculating distances, I got the expanded copy of the map
using `flatMapIndexed` for rows and `mapIndexed` for columns.

For the second part, with such a big growth factor, it was obvious the map must not really expand.
Instead, when calculating distances, the count of rows and columns to expand between the two galaxies
multiplied by the expansion factor minus 1 must be added to the original distance.
This solution of course works for the first part too.

[aoc]: https://adventofcode.com

[github]: https://github.com/radiokot

[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues

[kotlin]: https://kotlinlang.org

[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up

[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
