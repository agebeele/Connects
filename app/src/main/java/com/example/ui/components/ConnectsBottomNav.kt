package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoNavBg
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.viewmodel.MainTab

@Composable
fun ConnectsBottomNav(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = BentoNavBg,
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BentoBorderLight)
    ) {
        // Tab Álbum (Feed)
        NavigationBarItem(
            selected = currentTab == MainTab.Feed,
            onClick = { onTabSelected(MainTab.Feed) },
            icon = {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Álbum",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Álbum",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == MainTab.Feed) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoDarkNavy,
                selectedTextColor = BentoDarkNavy,
                indicatorColor = BentoCardBlue,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_feed")
        )

        // Tab Salas
        NavigationBarItem(
            selected = currentTab == MainTab.Rooms,
            onClick = { onTabSelected(MainTab.Rooms) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Salas",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Salas",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == MainTab.Rooms) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoDarkNavy,
                selectedTextColor = BentoDarkNavy,
                indicatorColor = BentoCardBlue,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_rooms")
        )

        // Tab Cámara (Snap center item)
        NavigationBarItem(
            selected = currentTab == MainTab.Camera,
            onClick = { onTabSelected(MainTab.Camera) },
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Cámara",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Cámara",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == MainTab.Camera) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoDarkNavy,
                selectedTextColor = BentoDarkNavy,
                indicatorColor = BentoCardBlue,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_camera")
        )

        // Tab Película IA
        NavigationBarItem(
            selected = currentTab == MainTab.MovieStudio,
            onClick = { onTabSelected(MainTab.MovieStudio) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Película IA",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Película IA",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == MainTab.MovieStudio) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoDarkNavy,
                selectedTextColor = BentoDarkNavy,
                indicatorColor = BentoCardBlue,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_movie_studio")
        )

        // Tab Perfil
        NavigationBarItem(
            selected = currentTab == MainTab.Profile,
            onClick = { onTabSelected(MainTab.Profile) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Perfil",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == MainTab.Profile) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoDarkNavy,
                selectedTextColor = BentoDarkNavy,
                indicatorColor = BentoCardBlue,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_profile")
        )
    }
}

