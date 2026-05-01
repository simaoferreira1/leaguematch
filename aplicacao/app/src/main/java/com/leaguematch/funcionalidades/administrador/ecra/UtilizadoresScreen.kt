package com.leaguematch.funcionalidades.administrador.ecra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary
import com.leaguematch.dados.modelos.TipoUtilizador
import com.leaguematch.dados.modelos.Utilizador

@Composable
fun UtilizadoresScreen(
    utilizadores: List<Utilizador> = listOf(
        Utilizador(1, "Rúben Ferreira", "ruben@leaguematch.pt", TipoUtilizador.ADMIN, true, 1, 3, 9),
        Utilizador(2, "Diogo Gomes", "diogo@leaguematch.pt", TipoUtilizador.ORGANIZADOR, true, 2, 4, 12),
        Utilizador(3, "João Fernandes", "joao@leaguematch.pt", TipoUtilizador.PARTICIPANTE, false, 1, 2, 7),
        Utilizador(4, "Simão Rodrigues Ferreira", "fsimao530@gmail.com", TipoUtilizador.PARTICIPANTE, true, 2, 4, 12)
    ),
    onUtilizadorClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "utilizadores",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = {},
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Gestão de Utilizadores",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Consulta e gestão dos utilizadores registados na aplicação.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            UtilizadoresResumoCard(
                totalUtilizadores = utilizadores.size,
                totalPerfis = utilizadores.map { it.tipo }.distinct().size
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Lista de utilizadores",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            utilizadores.forEach { utilizador ->
                UtilizadorCard(
                    nome = utilizador.nome,
                    perfil = utilizador.tipo.descricao,
                    ativo = utilizador.ativo,
                    onClick = { onUtilizadorClick(utilizador.id) }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun UtilizadoresResumoCard(
    totalUtilizadores: Int = 150,
    totalPerfis: Int = 4
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark)))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Total de utilizadores",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )

                Text(
                    text = totalUtilizadores.toString(),
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$totalPerfis perfis registados",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(52.dp)
            )
        }
    }
}

@Composable
fun UtilizadorCard(
    nome: String,
    perfil: String,
    ativo: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nome,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = perfil,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = if (ativo) "Ativo" else "Inativo",
                color = if (ativo) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilizadoresScreenPreview() {
    LeagueMatchTheme {
        UtilizadoresScreen()
    }
}
