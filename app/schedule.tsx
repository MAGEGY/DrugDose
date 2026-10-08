import { useMemo, useState } from 'react'
import { ScrollView, View, StyleSheet } from 'react-native'
import { Card, Chip, Text, TextInput } from 'react-native-paper'
import { SCHEDULE_OPTIONS } from '../src/lib/doseLogic'

export default function ScheduleScreen() {
  const [option, setOption] = useState(SCHEDULE_OPTIONS[1])
  const [drugName, setDrugName] = useState('')

  const times = useMemo(() => option.times, [option])

  return (
    <ScrollView style={styles.scroll} contentContainerStyle={styles.content}>
      <Card style={styles.card}>
        <Card.Title title="Frequency" />
        <Card.Content>
          <View style={styles.chips}>
            {SCHEDULE_OPTIONS.map((o) => (
              <Chip key={o.id} selected={option.id === o.id} onPress={() => setOption(o)} style={styles.chip}>
                {o.label}
              </Chip>
            ))}
          </View>
        </Card.Content>
      </Card>

      <Card style={styles.card}>
        <Card.Title title="Schedule" />
        <Card.Content>
          <TextInput
            label="Medicine name (optional)"
            value={drugName}
            onChangeText={setDrugName}
            mode="outlined"
            style={styles.input}
          />
          <Text style={styles.subtitle}>
            {drugName ? `${drugName} — ` : ''}
            {option.timesPerDay} dose{option.timesPerDay > 1 ? 's' : ''} per day
          </Text>
          <View style={styles.timeGrid}>
            {times.map((t) => (
              <View key={t} style={styles.timeBox}>
                <Text style={styles.time}>{t}</Text>
              </View>
            ))}
          </View>
          <Text style={styles.muted}>
            Times are suggestions spaced across waking hours - adjust to your routine and keep roughly even gaps.
          </Text>
        </Card.Content>
      </Card>
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  scroll: { flex: 1, backgroundColor: '#f0f9ff' },
  content: { padding: 12, gap: 12 },
  card: { borderRadius: 12 },
  chips: { flexDirection: 'row', flexWrap: 'wrap', gap: 6 },
  chip: { margin: 2 },
  input: { backgroundColor: '#fff', marginBottom: 10 },
  subtitle: { fontSize: 17, fontWeight: 'bold', marginBottom: 10, color: '#0e7490' },
  timeGrid: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 10 },
  timeBox: { backgroundColor: '#0e7490', borderRadius: 10, paddingVertical: 10, paddingHorizontal: 18 },
  time: { color: '#fff', fontWeight: 'bold', fontSize: 16 },
  muted: { color: '#64748b', fontSize: 13 },
})
