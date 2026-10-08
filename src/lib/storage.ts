import AsyncStorage from '@react-native-async-storage/async-storage'
import type { SavedCalculation } from './types'

const KEY = 'drugdose_history'

export async function getHistory(): Promise<SavedCalculation[]> {
  try {
    return JSON.parse((await AsyncStorage.getItem(KEY)) ?? '[]') as SavedCalculation[]
  } catch {
    return []
  }
}

export async function saveCalculation(calc: Omit<SavedCalculation, 'id' | 'timestamp'>): Promise<void> {
  const history = await getHistory()
  const entry: SavedCalculation = {
    ...calc,
    id: Date.now().toString(36) + Math.random().toString(36).slice(2, 6),
    timestamp: Date.now(),
  }
  await AsyncStorage.setItem(KEY, JSON.stringify([entry, ...history].slice(0, 100)))
}

export async function deleteCalculation(id: string): Promise<void> {
  const history = await getHistory()
  await AsyncStorage.setItem(KEY, JSON.stringify(history.filter((h) => h.id !== id)))
}
