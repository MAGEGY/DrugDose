import { useMemo, useState } from 'react'
import { ScrollView, View, StyleSheet } from 'react-native'
import { Button, Card, Chip, HelperText, Text, TextInput } from 'react-native-paper'
import { DRUG_PRESETS, calculateDose } from '../src/lib/doseLogic'
import type { DrugPreset } from '../src/lib/doseLogic'
import { saveCalculation } from '../src/lib/storage'

export default function CalculatorScreen() {
  const [preset, setPreset] = useState<DrugPreset>(DRUG_PRESETS[0])
  const [weight, setWeight] = useState('')
  const [timesPerDay, setTimesPerDay] = useState(String(DRUG_PRESETS[0].defaultTimesPerDay))
  const [saved, setSaved] = useState(false)

  const result = useMemo(
    () => calculateDose(preset, Number(weight), Number(timesPerDay)),
    [preset, weight, timesPerDay],
  )

  async function onSave() {
    if (!result) return
    await saveCalculation({
      drugName: preset.name,
      weightKg: Number(weight),
      timesPerDay: Number(timesPerDay),
      dosePerAdmin: result.dosePerAdmin,
      dailyTotal: result.dailyTotal,
      unit: preset.unit,
    })
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  return (
    <ScrollView style={styles.scroll} contentContainerStyle={styles.content}>
      <Card style={styles.card}>
        <Card.Title title="Select drug" />
        <Card.Content>
          <View style={styles.chips}>
            {DRUG_PRESETS.map((p) => (
              <Chip
                key={p.id}
                selected={preset.id === p.id}
                onPress={() => {
                  setPreset(p)
                  setTimesPerDay(String(p.defaultTimesPerDay))
                }}
                style={styles.chip}
              >
                {p.name}
              </Chip>
            ))}
          </View>
          <HelperText type="info">{preset.note}</HelperText>
        </Card.Content>
      </Card>

      <Card style={styles.card}>
        <Card.Title title="Patient" />
        <Card.Content>
          <TextInput
            label="Weight (kg)"
            value={weight}
            onChangeText={setWeight}
            keyboardType="decimal-pad"
            mode="outlined"
            style={styles.input}
          />
          <TextInput
            label="Doses per day"
            value={timesPerDay}
            onChangeText={setTimesPerDay}
            keyboardType="number-pad"
            mode="outlined"
            style={styles.input}
          />
        </Card.Content>
      </Card>

      {result && (
        <Card style={[styles.card, result.exceedsDailyMax && styles.warningCard]}>
          <Card.Title title="Result" />
          <Card.Content>
            <Text style={styles.bigDose}>
              {result.dosePerAdmin} {preset.unit} per dose
            </Text>
            <Text style={styles.sub}>
              {timesPerDay}× per day = {result.dailyTotal} {preset.unit}/day
            </Text>
            <Text style={styles.sub}>
              Daily maximum: {result.maxDailyAllowed} {preset.unit}
            </Text>
            {result.cappedAtMaxDose && (
              <Text style={styles.warning}>⚠ Dose capped at the single-dose maximum.</Text>
            )}
            {result.exceedsDailyMax && (
              <Text style={styles.warning}>⚠ Daily total exceeds the maximum allowed.</Text>
            )}
            {result.warnings.map((w) => (
              <Text key={w} style={styles.warning}>
                {w}
              </Text>
            ))}
            <Button mode="contained" onPress={onSave} style={styles.saveBtn}>
              {saved ? 'Saved ✓' : 'Save to history'}
            </Button>
          </Card.Content>
        </Card>
      )}

      <Text style={styles.disclaimer}>
        Educational tool only - always verify doses against local guidelines and the prescription.
      </Text>
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  scroll: { flex: 1, backgroundColor: '#f0f9ff' },
  content: { padding: 12, gap: 12 },
  card: { borderRadius: 12 },
  chips: { flexDirection: 'row', flexWrap: 'wrap', gap: 6 },
  chip: { margin: 2 },
  input: { marginBottom: 10, backgroundColor: '#fff' },
  bigDose: { fontSize: 26, fontWeight: 'bold', color: '#0e7490' },
  sub: { fontSize: 15, marginTop: 4, color: '#475569' },
  warning: { color: '#b45309', marginTop: 6, fontWeight: '600' },
  warningCard: { borderColor: '#f59e0b', borderWidth: 2 },
  saveBtn: { marginTop: 12 },
  disclaimer: { textAlign: 'center', color: '#64748b', fontSize: 12, padding: 12 },
})
