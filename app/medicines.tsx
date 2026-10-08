import { useEffect, useState } from 'react'
import { ScrollView, View, StyleSheet } from 'react-native'
import { ActivityIndicator, Card, Searchbar, Text } from 'react-native-paper'
import { parseMedicinesCsv, searchMedicines } from '../src/lib/medicines'
import type { Medicine } from '../src/lib/types'

export default function MedicinesScreen() {
  const [all, setAll] = useState<Medicine[]>([])
  const [query, setQuery] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    // Resolve the CSV against the page base URL so it works under the
    // GitHub Pages project path (/DrugDose/) and in local dev.
    const base =
      typeof document !== 'undefined'
        ? new URL('medicines.csv', document.baseURI).href
        : 'https://magegy.github.io/DrugDose/medicines.csv'
    fetch(base)
      .then((r) => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`)
        return r.text()
      })
      .then((csv) => {
        setAll(parseMedicinesCsv(csv))
        setLoading(false)
      })
      .catch((e) => {
        setError(`Failed to load medicine database: ${e.message}`)
        setLoading(false)
      })
  }, [])

  const results = searchMedicines(query, all)

  return (
    <ScrollView style={styles.scroll} contentContainerStyle={styles.content}>
      <Searchbar
        placeholder="Search brand or generic name..."
        value={query}
        onChangeText={setQuery}
        style={styles.search}
      />
      {loading && <ActivityIndicator size="large" style={styles.loader} />}
      {error ? <Text style={styles.error}>{error}</Text> : null}
      {!loading && !error && query.length === 0 && (
        <Text style={styles.muted}>Type to search {all.length} medicines (Egyptian market, prices in EGP).</Text>
      )}
      {!loading && query.length > 0 && results.length === 0 && (
        <Text style={styles.muted}>No matches for "{query}".</Text>
      )}
      {results.map((m) => (
        <Card key={m.id} style={styles.card}>
          <Card.Content>
            <View style={styles.row}>
              <Text style={styles.brand}>{m.brandName}</Text>
              <Text style={styles.price}>{m.price} EGP</Text>
            </View>
            <Text style={styles.muted}>
              {m.genericName} · {m.composition}
            </Text>
          </Card.Content>
        </Card>
      ))}
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  scroll: { flex: 1, backgroundColor: '#f0f9ff' },
  content: { padding: 12, gap: 8 },
  search: { backgroundColor: '#fff', marginBottom: 6 },
  loader: { marginTop: 40 },
  card: { borderRadius: 10 },
  row: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  brand: { fontWeight: 'bold', flex: 1, marginRight: 8 },
  price: { color: '#0e7490', fontWeight: 'bold' },
  muted: { color: '#64748b', fontSize: 13 },
  error: { color: '#dc2626', padding: 16 },
})
