import { Tabs } from 'expo-router'
import { PaperProvider } from 'react-native-paper'
import { StatusBar } from 'expo-status-bar'
import { useColorScheme } from 'react-native'

export default function RootLayout() {
  const scheme = useColorScheme()
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
    </PaperProvider>
  )
}
