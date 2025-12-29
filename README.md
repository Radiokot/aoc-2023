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

## [Day 12](https://adventofcode.com/2023/day/12), one star

For the first part, I decided to brute-force all the toggle (on/off) combinations for unknown indices.
When brute-forcing toggle combinations, it is convenient to think of each combination as a binary number
where each bit represent each toggle state.
For example, combinations for `??` in form of binary numbers are: `0(00)`, `1(01)`, `2(10)`, `3(11)`.
To check them all, iterate from `0(00)` until `4(100)`.

For the second part, brute-force doesn't work. I had a thought that perhaps the result is somehow a combination of
arrangement counts for start (`record + ?`), middle (`? + record + ?`) and end (`? + record`)
parts of an unfold record. The following formula works for some test cases:

```kotlin
arrangementsForEndingWithUnknown *
        arrangementsForSurroundedWithUnknown *
        arrangementsForSurroundedWithUnknown *
        arrangementsForSurroundedWithUnknown *
        arrangementsForStartingWithUnknown
```

I tried finding a hint, it is suggested to solve this part recursively, with memoization.
However, at the moment I just don't get what the recursion is here.

## [Day 13](https://adventofcode.com/2023/day/13)

First of all, I spent some time parsing the input, as the patterns have different dimensions and I couldn't
just chunk the list of lines.
Then I implemented a function that looks for a vertical mirror
and returns the number of columns to the left of it. The function iterates over all the reasonable
column counts and for each count checks if every pattern line is symmetrical (`leftPart == rightPart.reversed()`)
if split after this column. Initially I wanted to use `substring()` in this function, but I quickly tired
of being confused with index/count or inclusive/exclusive and ended up using
more intuitive `drop()` and `take()` functions instead.

As for the horizontal mirror search – I just transposed the pattern turning columns into lines,
and re-used the vertical mirror function.

For the second part, I copied the vertical mirror search function and adjusted it
to look not for a perfect reflection, but rather for a number of differences between supposed reflections.
If there is a single difference, then the dirty mirror is found. Therefore, my program doesn't know
the exact smudge location – just that it is there somewhere.

## [Day 14](https://adventofcode.com/2023/day/14)

For the first part, I implemented rock rolling (falling) function that for each `O`
finds the furthest free space above, before any `#` or other `O`,
and if there is such a place – moves the `O` there. For the total load calculation function,
I used a `foldIndexed()` expression.

For the second part, I first copy-pasted map clockwise rotation function from the year 2025
and implemented a single cycle function, which rolls the rocks north and rotates the map clockwise 4 times.

Then I just wanted to check if it could just be repeated 1,000,000,000 times, but it seemed to take forever.
Then I started printing the map after each cycle hoping that the output was going to repeat itself after
certain number of iterations. It did, but having realized this I didn't immediately understand
how to programmatically figure out the repeating chunk and how to get the answer from it.

To figure out the repeating chunk, I decided to make 10,000 iterations saving the total load.
Then, having the list of observed values, I looked for a number from 2 to 1,000 that,
if used as the list chunk size, would result in having the least amount of distinct chunks.
Then I just took the chunk of the found size.

```kotlin
val repeatingChunkSize = (2..1000).minBy { chunkSizeToCheck ->
    observedTotals
        .chunked(chunkSizeToCheck)
        .distinct()
        .size
}
val repeatingChunk = observedTotals.takeLast(repeatingChunkSize)
```

At this moment, 10,000 iterations are done, there are 999,990,000 iterations to go,
but the output repeats itself and the repeating chunk is obtained. This means the result
is somewhere in the repeating chunk.
Through trial and error, I figured out the "formula" for the answer,
in which I don't understand the purpose of `- 1`:

```kotlin
return repeatingChunk[(1000000000 - observedTotals.size - 1) % repeatingChunkSize]
```

## [Day 15](https://adventofcode.com/2023/day/15)

Straightforward solution – just coded what was requested in the problem.

## [Day 16](https://adventofcode.com/2023/day/16)

Another map traveling problem. Although I reused a lot of 2D code from [Day 10](#day-10),
the solution ended up quite verbose anyway.

For the first part, I implemented a `BeamTip` class which is aware of its current position and direction,
and can return tips for the next step.
So, if it is currently at a splitter, it returns 2 new tips at the sides of it, with opposite directions,
and so on, and so on. Got massive `when { }` for all the cases. Tips reached a wall (edge) are discarded.

Having the start `BeamTip`, I implemented the propagation loop with a queue for next tips,
which runs until the queue is empty and tracks energized positions.
Immediately realized my beams were trapped in an infinite loop.
Added tracking of visited splitters to discard split beams for subsequent visits – worked for part 1.

For the second part, I created not one start `BeamTip` but many, for each position along each wall,
then checked them all in search for a maximum energized position count. Initially the check didn't end.
I tried making checks `async`, but it didn't help.
Then I realized not only splitters but also mirrors cause infinite loops.
Adjusted the propagation function not to track splitters, but rather positions + directions –
if a position has been visited with the same direction, do not proceed. It helped, the answer is calculated
rather almost instantly, and `async` really did speed it up.

## [Day 17](https://adventofcode.com/2023/day/17)

I initially tried implementing breadth-first search, but the loop was endless.
Eventually solved this day with a hint to use Dijkstra algorithm, with priority queue.
For this problem, a node is a crucible state (position, direction, straight step count),
adjacent nodes are positions to which the crucible can move from the current state,
and a weight is a heat loss.

Part 2 differs from part 1 in how next possible states (adjacent nodes) are selected
and which states can actually stop at the finish (an ultra crucible from part 2 can't just stop).

Not taking into account my pre-Dijkstra attempts,
I spent considerable time debugging the start state and the step counting:

- Start state direction (right or down) doesn't matter unless there are bugs in the solution
- Start state straight step count is 0 and so the heat loss, because it just *appears* on the map
- When the crucible turns and advances in the new direction, its straight step count becomes 1, not 0

[aoc]: https://adventofcode.com

[github]: https://github.com/radiokot

[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues

[kotlin]: https://kotlinlang.org

[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up

[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
