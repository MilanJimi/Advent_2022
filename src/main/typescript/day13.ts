import fs from 'fs'

const input = fs.readFileSync("../resources/13/full.in")
    .toString()
    .split("\n\n")
    .map((it) => it.split("\n")
        .map(str => JSON.parse(str)))

type Nest = number | Array<Nest>

const compare = (first: Nest, second: Nest): -1 | 0 | 1 => {

    if (!Array.isArray(first) && !Array.isArray(second)) {
        if (first > second) return 1
        if (second > first) return -1
        if (second === first) return 0
    }

    if (Array.isArray(first) && Array.isArray(second)) {
        for (let i = 0; i < Math.max(first.length, second.length); i++) {
            if (i >= first.length) return -1
            if (i >= second.length) return 1
            const comp = compare(first[i], second[i])
            if (comp !== 0) return comp
        }
        return 0
    }

    if (Array.isArray(first) && !Array.isArray(second))
        return compare(first, [second])

    if (!Array.isArray(first) && Array.isArray(second))
        return compare([first], second)

    throw Error(`Could not find any branch for ${first} | ${second}`)

}

let indexSum = 0
for (let i = 0; i < input.length; i++) {
    const [first, second] = input[i]
    const comp = compare(first, second)
    if (comp === -1) indexSum += i + 1
}

const dividerPackets = [[[2]], [[6]]]
const flatInput = input.flat()
const sorted = [...flatInput, ...dividerPackets].sort(compare)

let dividerPacketsLocations = 1
for (let i = 0; i < sorted.length; i++) {
    if (dividerPackets.includes(sorted[i])) dividerPacketsLocations = dividerPacketsLocations * (i + 1)
}


console.log(`Part One: ${indexSum}`)
console.log(`Part Two: ${dividerPacketsLocations}`)