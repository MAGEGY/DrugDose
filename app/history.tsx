import { useCallback, useEffect, useState } from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import { Button, Card, Text } from 'react-native-paper'
import { deleteCalculation, getHistory } from '../src/lib/storage'
import type { SavedCalculation } from '../src/lib/types'

export default function HistoryScreen() {
  const [history, setHistory] = useState<SavedCalculation[]>([])

  const refresh = useCallback(() => {
    getHistory().then(setHistory)
  }, [])

  useEffect(() => {
    refresh()
  }, [refresh])

  return (
    <ScrollView style={styles.scroll} contentContainerStyle={styles.content}>
      {history.length === 0 && (
        <Text style={styles.muted}>No saved doses yet. Calculate a dose and tap "Save to history".</Text>
      )}
      {history.map((h) => (
        <Card key={h.id} style={styles.card}>
          <Card.Content>
            <View style={styles.row}>
              <Text style={styles.drug}>{h.drugName}</Text>
              <Text style={styles.dose}>
                {h.dosePerAdmin} {h.unit} × {h.timesPerDay}/day
              </Text>
            </View>
            <Text style={styles.muted}>
              {h.weightKg} kg · daily total {h.dailyTotal} {h.unit} · {new Date(h.timestamp).toLocaleString()}
            </Text>
            <Button
              mode="text"
              textColor="#dc2626"
              onPress={async () => {
                await deleteCalculation(h.id)
                refresh()
              }}
              style={styles.deleteBtn}
            >
              Delete
            </Button>
          </Card.Content>
        </Card>
      ))}
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  scroll: { flex: 1, backgroundColor: '#f0f9ff' },
  content: { padding: 12, gap: 10 },
  card: { borderRadius: 12 },
  row: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 4 },
  drug: { fontWeight: 'bold', fontSize: 16, flex: 1, marginRight: 8 },
  dose: { color: '#0e7490', fontWeight: 'bold' },
  muted: { color: '#64748b', fontSize: 13 },
  deleteBtn: { alignSelf: 'flex-end' },
})
