package com.tw.customsplashjetpack

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.tw.customsplashjetpack.ui.theme.CustomSplashJetpackTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomSplashJetpackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Log.e(this.javaClass.name, "onCreate: $innerPadding" )
                    SplashComposable()
                }
            }
        }
    }
}


@Composable
 fun SplashComposable() {
    val view = LocalView.current
    SideEffect {
        requestFullScreen(view)
    }

    Column (
        Modifier.background( color= Color.Magenta ).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        // ... other content
        Image(
            colorFilter = ColorFilter.tint(Color.Black),
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "Gmail",
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
        )

        Spacer(Modifier.height(20.dp))

        LocalView.current.context.getActivity()?.resources?.let {
            Text(text = it.getString(R.string.app_name),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
        }
    }

    Handler(Looper.getMainLooper()).postDelayed({

        Log.e("TAG", "gotoNextScreen: " )
        val intent = Intent(view.context, HomeScreen::class.java)
        view.context.getActivity()?.startActivity(intent)
        view.context.getActivity()?.finish()

    }, 2500)
}



fun requestFullScreen(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    WindowCompat.getInsetsController(window, view).hide(
        WindowInsetsCompat.Type.statusBars() or
                WindowInsetsCompat.Type.navigationBars()
    )
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    // this recursion should be okay since we call getActivity on a view context
    // that should have an Activity as its baseContext at some point
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}