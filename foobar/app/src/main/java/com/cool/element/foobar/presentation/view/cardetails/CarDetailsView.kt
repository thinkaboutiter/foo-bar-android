package com.cool.element.foobar.presentation.view.cardetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cool.element.foobar.domain.entity.application.CarApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CarDetailsView(
    modifier: Modifier = Modifier,
    car: CarApp,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (car.imageUrlString.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(car.imageUrlString),
                contentDescription = car.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(text = car.title)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = car.details)
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailsViewPreview() {
    CarDetailsView(
        car = CarApp(
            title = "Toyota Corolla",
            aboutUrlString = "https://example.com/car-models/toyota-corolla",
            details = "The Toyota Corolla is a compact car that has been popular for decades due to its reliability and fuel efficiency.",
            imageUrlString = "https://example.com/thumbnails/toyota-corolla.jpg"
        )
    )
}