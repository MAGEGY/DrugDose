export interface Medicine {
  id: number
  genericName: string
  brandName: string
  price: number
  composition: string
}

export interface SavedCalculation {
  id: string
  drugName: string
  weightKg: number
  timesPerDay: number
  dosePerAdmin: number
  dailyTotal: number
  unit: string
  timestamp: number
}
