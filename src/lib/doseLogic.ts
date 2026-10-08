// Drug dose presets and calculation logic.
// Standard pediatric references; always verify against local guidelines.
export interface DrugPreset {
  id: string
  name: string
  mgPerKgPerDose: number
  defaultTimesPerDay: number
  maxPerDose: number
  maxMgPerKgPerDay: number | null
  maxPerDayAbsolute: number
  unit: 'mg' | 'ml'
  note: string
}

export const DRUG_PRESETS: DrugPreset[] = [
  { id: 'paracetamol', name: 'Paracetamol', mgPerKgPerDose: 15, defaultTimesPerDay: 4, maxPerDose: 1000, maxMgPerKgPerDay: 75, maxPerDayAbsolute: 4000, unit: 'mg', note: 'Antipyretic/analgesic. Max 75 mg/kg/day, never exceed 4 g/day.' },
  { id: 'ibuprofen', name: 'Ibuprofen', mgPerKgPerDose: 10, defaultTimesPerDay: 3, maxPerDose: 400, maxMgPerKgPerDay: 40, maxPerDayAbsolute: 1200, unit: 'mg', note: 'NSAID. Avoid under 3 months of age. Give with food.' },
  { id: 'amoxicillin', name: 'Amoxicillin', mgPerKgPerDose: 25, defaultTimesPerDay: 3, maxPerDose: 875, maxMgPerKgPerDay: 90, maxPerDayAbsolute: 2625, unit: 'mg', note: 'Standard dose 25 mg/kg/day divided TID; high-dose otitis 45-90 mg/kg/day.' },
  { id: 'azithromycin', name: 'Azithromycin', mgPerKgPerDose: 10, defaultTimesPerDay: 1, maxPerDose: 500, maxMgPerKgPerDay: 10, maxPerDayAbsolute: 500, unit: 'mg', note: 'Once daily for 3-5 days depending on indication.' },
  { id: 'cefixime', name: 'Cefixime', mgPerKgPerDose: 4, defaultTimesPerDay: 2, maxPerDose: 200, maxMgPerKgPerDay: 8, maxPerDayAbsolute: 400, unit: 'mg', note: '8 mg/kg/day divided BID.' },
  { id: 'diclofenac', name: 'Diclofenac', mgPerKgPerDose: 1, defaultTimesPerDay: 2, maxPerDose: 50, maxMgPerKgPerDay: 3, maxPerDayAbsolute: 150, unit: 'mg', note: 'Max 150 mg/day in adults.' },
  { id: 'cetirizine', name: 'Cetirizine', mgPerKgPerDose: 0.25, defaultTimesPerDay: 1, maxPerDose: 10, maxMgPerKgPerDay: 0.5, maxPerDayAbsolute: 10, unit: 'mg', note: 'Antihistamine, once daily.' },
  { id: 'prednisolone', name: 'Prednisolone', mgPerKgPerDose: 1, defaultTimesPerDay: 1, maxPerDose: 60, maxMgPerKgPerDay: 2, maxPerDayAbsolute: 60, unit: 'mg', note: '1-2 mg/kg/day, usually once daily in the morning.' },
  { id: 'salbutamol', name: 'Salbutamol (oral)', mgPerKgPerDose: 0.15, defaultTimesPerDay: 3, maxPerDose: 4, maxMgPerKgPerDay: 0.6, maxPerDayAbsolute: 12, unit: 'mg', note: 'Oral bronchodilator; inhaled is preferred in acute attacks.' },
  { id: 'omeprazole', name: 'Omeprazole', mgPerKgPerDose: 1, defaultTimesPerDay: 1, maxPerDose: 40, maxMgPerKgPerDay: 2, maxPerDayAbsolute: 40, unit: 'mg', note: 'Proton pump inhibitor, before breakfast.' },
  { id: 'metronidazole', name: 'Metronidazole', mgPerKgPerDose: 10, defaultTimesPerDay: 3, maxPerDose: 500, maxMgPerKgPerDay: 45, maxPerDayAbsolute: 1500, unit: 'mg', note: '30-45 mg/kg/day divided TID.' },
  { id: 'sulfonyl', name: 'ORS (oral rehydration)', mgPerKgPerDose: 10, defaultTimesPerDay: 4, maxPerDose: 500, maxMgPerKgPerDay: null, maxPerDayAbsolute: 2000, unit: 'ml', note: '10 ml/kg per loose stool is a common maintenance rule.' },
]

export interface DoseResult {
  dosePerAdmin: number
  dailyTotal: number
  maxDailyAllowed: number
  exceedsDailyMax: boolean
  cappedAtMaxDose: boolean
  warnings: string[]
}

export function calculateDose(
  preset: DrugPreset,
  weightKg: number,
  timesPerDay: number,
): DoseResult | null {
  if (!weightKg || weightKg <= 0 || !timesPerDay || timesPerDay <= 0) return null

  const warnings: string[] = []
  const rawDose = weightKg * preset.mgPerKgPerDose
  const cappedAtMaxDose = rawDose > preset.maxPerDose
  const dosePerAdmin = Math.round(Math.min(rawDose, preset.maxPerDose) * 100) / 100
  const dailyTotal = Math.round(dosePerAdmin * timesPerDay * 100) / 100

  const maxDailyAllowed = preset.maxMgPerKgPerDay
    ? Math.round(Math.min(weightKg * preset.maxMgPerKgPerDay, preset.maxPerDayAbsolute) * 100) / 100
    : preset.maxPerDayAbsolute

  if (cappedAtMaxDose) {
    warnings.push(`Weight-based dose (${Math.round(rawDose)} ${preset.unit}) exceeds the single-dose maximum of ${preset.maxPerDose} ${preset.unit} - capped.`)
  }
  if (dailyTotal > maxDailyAllowed) {
    warnings.push(`Daily total (${dailyTotal} ${preset.unit}) exceeds the daily maximum of ${maxDailyAllowed} ${preset.unit}.`)
  }
  if (weightKg > 80) {
    warnings.push('Weight above 80 kg - adult dosing rules usually apply instead of mg/kg.')
  }

  return { dosePerAdmin, dailyTotal, maxDailyAllowed, exceedsDailyMax: dailyTotal > maxDailyAllowed, cappedAtMaxDose, warnings }
}

// Dose schedule helper
export interface ScheduleOption {
  id: string
  label: string
  timesPerDay: number
  times: string[]
}

export const SCHEDULE_OPTIONS: ScheduleOption[] = [
  { id: 'od', label: 'OD - once daily', timesPerDay: 1, times: ['09:00'] },
  { id: 'bid', label: 'BID - twice daily', timesPerDay: 2, times: ['09:00', '21:00'] },
  { id: 'tid', label: 'TID - three times daily', timesPerDay: 3, times: ['07:00', '15:00', '23:00'] },
  { id: 'qid', label: 'QID - four times daily', timesPerDay: 4, times: ['07:00', '12:00', '17:00', '22:00'] },
  { id: 'q4h', label: 'Every 4 hours', timesPerDay: 6, times: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'] },
  { id: 'q6h', label: 'Every 6 hours', timesPerDay: 4, times: ['00:00', '06:00', '12:00', '18:00'] },
  { id: 'q8h', label: 'Every 8 hours', timesPerDay: 3, times: ['00:00', '08:00', '16:00'] },
  { id: 'qhs', label: 'QHS - at bedtime', timesPerDay: 1, times: ['21:00'] },
]
