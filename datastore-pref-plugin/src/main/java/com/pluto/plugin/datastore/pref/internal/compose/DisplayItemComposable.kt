package com.pluto.plugin.datastore.pref.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluto.plugin.datastore.pref.internal.PrefElement
import com.pluto.plugin.datastore.pref.internal.Type


@Composable
@Preview("normal item")
fun PreviewListItem() {
    LazyColumn {
        item {
            PrefListItem(
                element = PrefElement("key param", "value of the key", Type.TYPE_STRING),
                modifier = Modifier.background(Color(0xFFFFFFFF))
            )
        }
    }
}

@Composable
@Preview("very long item")
fun PreviewLongContentListItem() {
    LazyColumn {
        item {
            PrefListItem(
                element = PrefElement(
                    "VERY VERY VERY VERY VERY very very very very very very Loooong Key",
                    "VERY VERY VERY VERY VERY very very very very Loooong value",
                    Type.TYPE_BOOLEAN
                ),
                modifier = Modifier.background(Color(0xFFFFFFFF))
            )
        }
    }
}

@Composable
fun PrefListItem(element: PrefElement, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = element.key,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                fontSize = 12.sp,
                color = Color(0xFF78768B)
            )
            Text(
                text = element.type.displayText,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = Color(0xFFF1F7EF),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(bottom = 2.dp, start = 8.dp, end = 8.dp),
                color = Color(0xFF71B36B),
                fontSize = 8.sp,
            )
        }
        Text(
            text = element.value, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp)
        )
        Divider(Modifier.padding(top = 8.dp), color = Color(0xFFF3F3F2))
    }
}
