import type { Medicine } from './types'

// Parses the Egyptian medicines CSV bundled from the RxScanAI assets.
// Line format: "Generic,Brand,Price,""Composition"""
export function parseMedicinesCsv(csv: string): Medicine[] {
  const medicines: Medicine[] = []
  let id = 0

  for (const rawLine of csv.split(/\r?\n/)) {
    const line = rawLine.trim()
    if (!line) continue
    const unquoted = line.replace(/^"/, '').replace(/"$/, '')
    const fields = parseCsvLine(unquoted)
    if (fields.length < 4) continue
    const [generic, brand, price, composition] = fields
    if (generic === 'Generic Name') continue
    medicines.push({
      id: id++,
      genericName: generic,
      brandName: brand,
      price: Number(price.replace(/[^0-9.]/g, '')) || 0,
      composition,
    })
  }
  return medicines
}

function parseCsvLine(line: string): string[] {
  const fields: string[] = []
  let current = ''
  let inQuotes = false
  for (let i = 0; i < line.length; i++) {
    const c = line[i]
    if (c === '"') {
      if (inQuotes && line[i + 1] === '"') {
        current += '"'
        i++
      } else {
        inQuotes = !inQuotes
      }
    } else if (c === ',' && !inQuotes) {
      fields.push(current.trim())
      current = ''
    } else {
      current += c
    }
  }
  fields.push(current.trim())
  return fields
}

export function levenshteinDistance(s1: string, s2: string): number {
  const dp: number[][] = Array.from({ length: s1.length + 1 }, () => new Array(s2.length + 1).fill(0))
  for (let i = 0; i <= s1.length; i++) dp[i][0] = i
  for (let j = 0; j <= s2.length; j++) dp[0][j] = j
  for (let i = 1; i <= s1.length; i++) {
    for (let j = 1; j <= s2.length; j++) {
      const cost = s1[i - 1] === s2[j - 1] ? 0 : 1
      dp[i][j] = Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
    }
  }
  return dp[s1.length][s2.length]
}

export function searchMedicines(query: string, all: Medicine[], limit = 20): Medicine[] {
  const q = query.trim().toLowerCase()
  if (!q) return []
  const scored = all
    .map((m) => {
      const brand = m.brandName.toLowerCase()
      const generic = m.genericName.toLowerCase()
      let score = Number.MAX_SAFE_INTEGER
      if (brand.startsWith(q)) score = 0
      else if (brand.includes(q)) score = 2
      else if (generic.includes(q)) score = 3
      else {
        const words = `${brand} ${generic}`.split(/\s+/)
        let best = Number.MAX_SAFE_INTEGER
        for (const w of words) {
          if (w.startsWith(q)) best = Math.min(best, 1)
          else if (w.length > 3) {
            const d = levenshteinDistance(q, w)
            if (d <= 2) best = Math.min(best, 4 + d)
          }
        }
        score = best
      }
      return { m, score }
    })
    .filter((x) => x.score < Number.MAX_SAFE_INTEGER)
    .sort((a, b) => a.score - b.score)
    .map((x) => x.m)
  return scored.slice(0, limit)
}
