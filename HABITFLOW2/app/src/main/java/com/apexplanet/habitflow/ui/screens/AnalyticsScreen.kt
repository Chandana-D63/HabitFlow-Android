package com.apexplanet.habitflow.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apexplanet.habitflow.data.model.Habit
import com.apexplanet.habitflow.ui.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val habits by viewModel.allHabits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (habits.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Start tracking habits to see analytics!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(habits) { habit ->
                    HabitAnalyticsCard(habit, viewModel)
                }
            }
        }
    }
}

@Composable
fun HabitAnalyticsCard(habit: Habit, viewModel: HabitViewModel) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Current Streak", "${habit.streakCount} 🔥", MaterialTheme.colorScheme.tertiary)
                StatItem("Longest Streak", "${habit.longestStreak} 🏆", MaterialTheme.colorScheme.secondary)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Last 7 Days Consistency",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            val stats = viewModel.getCompletionStatsForLast7Days(habit)
            ConsistencyChart(stats)
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

@Composable
fun ConsistencyChart(stats: List<Boolean>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val spacing = width / (stats.size - 1)
            
            // Draw horizontal baseline
            drawLine(
                color = outlineColor,
                start = Offset(0f, height / 2),
                end = Offset(width, height / 2),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            stats.forEachIndexed { index, isCompleted ->
                val x = index * spacing
                val y = if (isCompleted) 0f else height
                
                // Draw point
                drawCircle(
                    color = if (isCompleted) primaryColor else outlineColor,
                    radius = 6.dp.toPx(),
                    center = Offset(x, height / 2)
                )
                
                if (isCompleted) {
                     drawLine(
                        color = primaryColor,
                        start = Offset(x, height / 2),
                        end = Offset(x, 0f),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
