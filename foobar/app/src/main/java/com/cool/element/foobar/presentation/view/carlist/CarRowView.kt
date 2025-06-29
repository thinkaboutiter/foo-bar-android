package com.cool.element.foobar.presentation.view.carlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cool.element.foobar.domain.entity.application.CarApp

@Composable
fun CarRowView(car: CarApp) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = car.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = car.details,
                style = MaterialTheme.typography.bodyMedium
            )
            // You could add AsyncImage here for car.imageUrlString
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarRowViewPreview() {
    CarRowView(
        car = CarApp(
            title = "Audi e-tron GT",
            aboutUrlString = "https://audi.com/e-tron-gt",
            details = "High-performance electric sports car.",
            imageUrlString = ""
        )
    )
}

