import { Tabs, useRouter } from 'expo-router'
import { PaperProvider } from 'react-native-paper'
import { StatusBar } from 'expo-status-bar'
import { useColorScheme } from 'react-native'
import FloatingHelp from '../components/FloatingHelp'
import type { HelpHowTo } from '../components/FloatingHelp'

const HELP_HOWTOS: HelpHowTo[] = [
  { title: 'Calculate a dose', body: 'On the Calculator tab, pick a drug, enter the patient weight in kg and the doses per day. The app computes the dose per administration and the daily total, and warns if a single-dose or daily maximum is exceeded.' },
  { title: 'Safety checks', body: 'Every drug preset has a maximum per dose and a maximum per day (mg/kg/day capped by an absolute limit). When the weight-based dose exceeds them, the dose is capped and a warning appears. Always double-check against the prescription.' },
  { title: 'Search medicines', body: 'The Medicines tab searches 1,071 Egyptian medicines by brand or generic name (fuzzy matching included), showing composition and price in EGP.' },
  { title: 'Plan dose times', body: 'The Schedule tab generates suggested clock times for OD, BID, TID, QID, every 4/6/8 hours, or bedtime dosing — spaced across waking hours.' },
  { title: 'Save and revisit', body: 'Tap "Save to history" after a calculation to store it. The History tab lists saved doses with weight, dose and date; you can delete entries there.' },
  { title: 'Educational tool', body: 'DrugDose is an educational aid, not a medical device. Always verify doses against local guidelines and the prescribing doctor.' },
]

export default function RootLayout() {
  const scheme = useColorScheme()
  const router = useRouter()
  return (
    <PaperProvider>
      <StatusBar style={scheme === 'dark' ? 'light' : 'dark'} />
      <Tabs
        screenOptions={{
          headerStyle: { backgroundColor: '#0e7490' },
          headerTintColor: '#fff',
          tabBarActiveTintColor: '#0e7490',
        }}
      >
        <Tabs.Screen name="index" options={{ title: 'Calculator', tabBarLabel: 'Calculator' }} />
        <Tabs.Screen name="medicines" options={{ title: 'Medicines', tabBarLabel: 'Medicines' }} />
        <Tabs.Screen name="schedule" options={{ title: 'Schedule', tabBarLabel: 'Schedule' }} />
        <Tabs.Screen name="history" options={{ title: 'History', tabBarLabel: 'History' }} />
      </Tabs>
      <FloatingHelp
        functions={[
          { label: 'Dose calculator', description: 'Weight-based dosing with safety maximums', action: () => router.navigate('/') },
          { label: 'Medicine reference', description: 'Search 1,071 Egyptian medicines with prices', action: () => router.navigate('/medicines') },
          { label: 'Dose schedule', description: 'Suggested clock times for each frequency', action: () => router.navigate('/schedule') },
          { label: 'Saved doses', description: 'Revisit your saved calculations', action: () => router.navigate('/history') },
        ]}
        howtos={HELP_HOWTOS}
      />
    </PaperProvider>
  )
}
