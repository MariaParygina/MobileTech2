package com.example.my2application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RecipeApp()
                }
            }
        }
    }
}

data class Recipe(
    val id: Int,
    val title: String,
    val difficulty: String,
    val portions: Int,
    val calories: Int,
)

data class RecipeDetails(
    val id: Int,
    val title: String,
    val category: String,
    val difficulty: String,
    val time: String,
    val portions: Int,
    val calories: Int,
    val ingredients: String,
    val description: String,
)

enum class RecipeFilter {
    ALL, WANT_TO_COOK, COOKING, COOKED
}

enum class RecipeStage {
    NONE, WANT_TO_COOK, COOKING, COOKED,
}

data class RecipeStatus(
    val stage: RecipeStage = RecipeStage.NONE,
)

val sampleRecipeList = listOf(
    Recipe(1, "Грибной крем-суп", "Новичок", 6, 26),
    Recipe(2, "Суп куриный с чечевицей", "Новичок", 7, 35),
    Recipe(3, "Курица в сливочном соусе на сковороде", "Новичок", 3, 158),
    Recipe(4, "Плов узбекский из говядины в казане", "Новичок", 8, 194),
    Recipe(5, "Лазанья с фаршем в духовке в соусе Бешамель", "Новичок", 6, 237),
)

val sampleRecipeDetailsList = listOf(
    RecipeDetails(
        id = 1,
        title = "Грибной крем-суп",
        category = "Суп",
        difficulty = "Новичок",
        time = "50 мин",
        portions = 6,
        calories = 26,
        ingredients = "Шампиньоны – 400 г\n" +
                "Лук репчатый – 1 шт.\n" +
                "Сливки – 150 мл 10%\n" +
                "Картофель – 4 шт.\n" +
                "Масло растительное – 2 ст.л.\n" +
                "Вода – 2 л\n" +
                "Соль – ч.л. по вкусу",
        description = "1. Обжарка: Репчатый лук нарезать кубиками, шампиньоны (или любые другие грибы) — пластинками. Обжарить их на сливочном масле до золотистого цвета и выпаривания жидкости (около 7–10 минут).\n" +
                "\n" +
                "2. Варка: В кастрюле вскипятить воду или бульон (куриный/овощной). Добавить обжаренные грибы с луком, нарезанный кубиками картофель. Варить 15–20 минут до мягкости картофеля.\n" +
                "\n" +
                "3. Измельчение: Снять кастрюлю с огня. Погружным блендером превратить содержимое в однородное пюре.\n" +
                "\n" +
                "4. Добавление сливок: Влить сливки (10–20%), добавить соль, перец, щепотку мускатного ореха. Тщательно перемешать.\n" +
                "\n" +
                "5. Завершение: Поставить суп на медленный огонь, прогреть (не кипятить!) в течение 2–3 минут. Подавать с гренками или сухариками и зеленью.",
    ),
    RecipeDetails(
        id = 2,
        title = "Суп куриный с чечевицей",
        category = "Суп",
        difficulty = "Новичок",
        time = "40 мин",
        portions = 7,
        calories = 35,
        ingredients = "Куриное филе – 500 г\n" +
                "Чечевица – 200 г\n" +
                "Картофель – 3 шт.\n" +
                "Морковь – 1 шт.\n" +
                "Лук репчатый – 1 шт.\n" +
                "Чеснок – 2 зубчика\n" +
                "Масло растительное – 2 ст.л.\n" +
                "Соль, перец, лавровый лист – по вкусу",
        description = "1. Подготовка: Куриные части (голень, бедро или филе) залить холодной водой, довести до кипения. Снять пену, убавить огонь и варить бульон 20–30 минут.\n" +
                "\n" +
                "2. Закладка овощей: Морковь натереть на крупной терке, лук мелко нарезать. Обжарить их на сковороде с маслом до мягкости. Картофель нарезать кубиками.\n" +
                "\n" +
                "3. Чечевица: В кастрюлю с бульоном высыпать промытую чечевицу (красную или зеленую) и нарезанный картофель. Варить 10–15 минут.\n" +
                "\n" +
                "4. Зажарка: Добавить в суп обжаренные лук с морковью, посолить, поперчить. Бросить лавровый лист.\n" +
                "\n" +
                "5. Завершение: Варить еще 5–7 минут до готовности всех ингредиентов. Выключить огонь, добавить измельченный чеснок и свежую зелень. Дать настояться под крышкой 10 минут перед подачей.",
    ),
    RecipeDetails(
        id = 3,
        title = "Курица в сливочном соусе на сковороде",
        category = "Горячее",
        difficulty = "Новичок",
        time = "50 мин",
        portions = 3,
        calories = 158,
        ingredients = "500 гр. филе куриного\n" +
                "250 гр. сливок нежирных\n" +
                "1 луковица\n" +
                "1 ч. л. муки\n" +
                "0,5 ч. л. соли и перца черного молотого\n" +
                "2-3 веточки укропа\n" +
                "2-3 ст. л. масла подсолнечного",
        description = "1. Подготовка: Филе нарезать небольшими кусочками, лук — кубиками, чеснок мелко порубить.\n" +
                "\n" +
                "2. Обжарка: В сковороде разогреть масло. Обжарить курицу на сильном огне до золотистой корочки (5-7 минут). Добавить лук с чесноком, жарить еще 2-3 минуты до прозрачности лука.\n" +
                "\n" +
                "3. Сливочная основа: Убавить огонь. Всыпать муку, быстро перемешать. Влить сливки, постоянно помешивая, чтобы не было комочков.\n" +
                "\n" +
                "4. Томление: Довести до кипения, убавить огонь до минимума. Посолить, поперчить. Тушить под крышкой 5-7 минут, пока соус слегка не загустеет.\n" +
                "\n" +
                "5. Подача: Снять с огня, посыпать рубленой зеленью. Отлично сочетается с пастой, рисом или картофельным пюре."
    ),
    RecipeDetails(
        id = 4,
        title = "Плов узбекский из говядины в казане",
        category = "Горячее",
        difficulty = "Продвинутый",
        time = "2 часа 30 минут",
        portions = 8,
        calories = 194,
        ingredients = "700 гр. говядины\n" +
                "500 гр. риса (длиннозерный)\n" +
                "400 гр. моркови\n" +
                "300 гр. лука\n" +
                "100 мл. растительного масла\n" +
                "20 гр. чеснока (1 головка)\n" +
                "1 стол.л. соли\n" +
                "1 чайн.л. барбариса\n" +
                "1 чайн.л. куркумы\n" +
                "1 чайн.л. паприки\n" +
                "1 чайн.л. зиры\n" +
                "перец черный молотый (по вкусу)",
        description = "1. Подготовка продуктов: Мясо нарезать крупными кусками (3-4 см). Лук — полукольцами, морковь — длинной соломкой (не тереть на терке!). Чеснок очистить от верхней шелухи, но оставить целой головкой.\n" +
                "\n" +
                "2. Перекаливание масла: В казане или толстостенной кастрюле разогреть масло до легкого дымка.\n" +
                "\n" +
                "3. Обжарка лука и мяса: В раскаленное масло выложить лук, жарить до золотистого цвета. Добавить мясо, жарить до румяной корочки.\n" +
                "\n" +
                "4. Добавление моркови: Выложить морковь ровным слоем, жарить, не перемешивая, 2-3 минуты, затем аккуратно перемешать и готовить еще 5-7 минут, пока морковь не станет мягче.\n" +
                "\n" +
                "5. Заливка кипятком (Зирвак): Влить кипяток так, чтобы он покрыл мясо с овощами. Добавить соль, зиру, барбарис (другие специи по желанию). Убавить огонь и тушить 30-40 минут (для говядины).\n" +
                "\n" +
                "6. Закладка риса: Рис промыть 5-7 раз до прозрачной воды, равномерно высыпать в казан. Залить кипятком на 2 пальца выше риса.\n" +
                "\n" +
                "7. Вдавливание чеснока: Когда вода впитается в рис (почти не будет видно жидкости), воткнуть в рис целую головку чеснока.\n" +
                "\n" +
                "8. Упаривание: Сделать в рисе палочкой отверстия до дна, чтобы выходил пар. Накрыть крышкой и томить на самом медленном огне 20-30 минут.\n" +
                "\n" +
                "9. Подача: Выключить огонь, дать постоять 10 минут. Аккуратно перемешать, выложить на большое блюдо.",
    ),
    RecipeDetails(
        id = 5,
        title = "Лазанья с фаршем в духовке в соусе Бешамель",
        category = "Горячее",
        difficulty = "Новичок",
        time = "2 часа 30 минут",
        portions = 6,
        calories = 237,
        ingredients = "800 гр. мясного фарша (любой, можно микс)\n" +
                "2 шт. помидоров\n" +
                "2 шт. небольшого лука\n" +
                "10 листов лазаньи (8-10 шт.)\n" +
                "2 зубч. чеснока\n" +
                "2 стол.л. томатной пасты\n" +
                "100 гр. твердого сыра\n" +
                "1 стол.л. растительного масла\n" +
                "соль (по вкусу)\n" +
                "\n" +
                "Для соуса:\n" +
                "50 гр. сливочного масла\n" +
                "600 мл. молока\n" +
                "5 стол.л. пшеничной муки (без горки)\n" +
                "душистый перец (по вкусу)\n" +
                "соль (по вкусу)",
        description = "1. Соус Бешамель: В сотейнике растопить сливочное масло, всыпать муку, быстро перемешать. Тонкой струйкой влить горячее молоко, постоянно мешая венчиком, чтобы не было комочков. Варить на медленном огне, помешивая, пока соус не загустеет (3–5 минут). Добавить соль и душистый перец.\n" +
                "2. Сборка: В форму для запекания выложить на дно немного соуса Бешамель. Сверху выложить листы лазаньи (сухие, если на пачке не указано иное). На листы — часть мясного соуса, затем немного Бешамеля. Повторить слои 3–4 раза (листы, мясо, бешамель). Верхний слой должен быть из листов, обильно смазанных Бешамелем.\n" +
                "3. Запекание: Посыпать верх тертым сыром. Накрыть форму фольгой и поставить в разогретую до 180°C духовку на 25 минут. Затем снять фольгу и запекать еще 10 минут до румяной корочки.\n" +
                "4. Подача: Достать лазанью из духовки, дать постоять 10–15 минут (чтобы лучше резалась и держала форму), затем нарезать на порции."
    ),
)

fun getRecipeDetailsById(id: Int): RecipeDetails? {
    return sampleRecipeDetailsList.find { it.id == id }
}

data class RecipeListUiState(
    val searchQuery: String = "",
    val recipeList: List<Recipe> = sampleRecipeList,
    val currentFilter: RecipeFilter = RecipeFilter.ALL,
    val recipeStatuses: Map<Int, RecipeStatus> = emptyMap()
)

class RecipeViewModel : ViewModel() {
    var uiState by mutableStateOf(RecipeListUiState())
        private set

    fun onSearchChange(newValue: String) {
        uiState = uiState.copy(
            searchQuery = newValue,
            recipeList = filterRecipes(newValue, uiState.currentFilter, uiState.recipeStatuses)
        )
    }

    fun onFilterChange(newFilter: RecipeFilter) {
        uiState = uiState.copy(
            currentFilter = newFilter,
            recipeList = filterRecipes(uiState.searchQuery, newFilter, uiState.recipeStatuses)
        )
    }

    fun onStatusChange(id: Int, newStatus: RecipeStatus) {
        val updatedStatuses = uiState.recipeStatuses + (id to newStatus)
        uiState = uiState.copy(
            recipeStatuses = updatedStatuses,
            recipeList = filterRecipes(uiState.searchQuery, uiState.currentFilter, updatedStatuses)
        )
    }

    fun getRecipeDetailsById(id: Int): RecipeDetails? {
        return sampleRecipeDetailsList.find { it.id == id }
    }

    private fun filterRecipes(
        query: String,
        filter: RecipeFilter,
        statuses: Map<Int, RecipeStatus>
    ): List<Recipe> {
        val searched = if (query.isBlank()) {
            sampleRecipeList
        } else {
            sampleRecipeList.filter { it.title.contains(query, ignoreCase = true) }
        }

        return searched.filter { recipe ->
            val status = statuses[recipe.id] ?: RecipeStatus()
            when (filter) {
                RecipeFilter.ALL -> true
                RecipeFilter.WANT_TO_COOK -> status.stage == RecipeStage.WANT_TO_COOK
                RecipeFilter.COOKING -> status.stage == RecipeStage.COOKING
                RecipeFilter.COOKED -> status.stage == RecipeStage.COOKED
            }
        }
    }
}

object RecipeRoutes {
    const val LIST_ROUTE = "recipe_list"
    const val DETAILS_ROUTE = "recipe_details"
    const val RECIPE_ID_ARG = "recipeId"

    const val DETAILS_ROUTE_PATTERN = "$DETAILS_ROUTE/{$RECIPE_ID_ARG}"
    fun details(recipeId: Int): String = "$DETAILS_ROUTE/$recipeId"
}

@Composable
fun RecipeApp() {
    val recipeViewModel: RecipeViewModel = viewModel()
    val uiState = recipeViewModel.uiState
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RecipeRoutes.LIST_ROUTE) {
        composable(RecipeRoutes.LIST_ROUTE) {
            RecipeListScreen(
                recipeList = uiState.recipeList,
                recipeStatuses = uiState.recipeStatuses,
                currentFilter = uiState.currentFilter,
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = recipeViewModel::onSearchChange,
                onFilterChange = recipeViewModel::onFilterChange,
                onStatusChange = recipeViewModel::onStatusChange,
                onRecipeClick = { recipeId ->
                    navController.navigate(RecipeRoutes.details(recipeId))
                }
            )
        }

        composable(
            route = RecipeRoutes.DETAILS_ROUTE_PATTERN,
            arguments = listOf(
                navArgument(
                    RecipeRoutes.RECIPE_ID_ARG
                ) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt(RecipeRoutes.RECIPE_ID_ARG)
            if (recipeId != null) {
                RecipeDetailsScreen(
                    recipeId = recipeId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    recipeList: List<Recipe>,
    recipeStatuses: Map<Int, RecipeStatus>,
    onStatusChange: (Int, RecipeStatus) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onRecipeClick: (Int) -> Unit,
    currentFilter: RecipeFilter,
    onFilterChange: (RecipeFilter) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Книга рецептов",
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Поиск по названию") },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                FilterChip(
                    onClick = { onFilterChange(RecipeFilter.ALL) },
                    label = { Text("Все рецепты") },
                    selected = currentFilter == RecipeFilter.ALL
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                FilterChip(
                    onClick = { onFilterChange(RecipeFilter.WANT_TO_COOK) },
                    label = { Text("Хочу сделать") },
                    selected = currentFilter == RecipeFilter.WANT_TO_COOK
                )
                FilterChip(
                    onClick = { onFilterChange(RecipeFilter.COOKING) },
                    label = { Text("В процессе") },
                    selected = currentFilter == RecipeFilter.COOKING
                )
                FilterChip(
                    onClick = { onFilterChange(RecipeFilter.COOKED) },
                    label = { Text("Приготовлено") },
                    selected = currentFilter == RecipeFilter.COOKED
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (recipeList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Рецепты не найдены")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = recipeList, key = { it.id }) { item ->
                        RecipeCard(
                            recipe = item,
                            status = recipeStatuses[item.id] ?: RecipeStatus(),
                            onClick = { onRecipeClick(item.id) },
                            onStatusChange = { newStatus ->
                                onStatusChange(item.id, newStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean
) {
    androidx.compose.material3.FilterChip(
        onClick = onClick,
        label = label,
        selected = selected,
        modifier = Modifier.wrapContentSize()
    )
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    status: RecipeStatus,
    onClick: () -> Unit,
    onStatusChange: (RecipeStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = recipe.title,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Сложность: ${recipe.difficulty}\n" +
                        "Кол-во порций: ${recipe.portions}\n" +
                        "Калорийность (100g): ${recipe.calories} ккал",
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val newStage = when (status.stage) {
                        RecipeStage.NONE -> RecipeStage.WANT_TO_COOK
                        RecipeStage.WANT_TO_COOK -> RecipeStage.COOKING
                        RecipeStage.COOKING -> RecipeStage.COOKED
                        RecipeStage.COOKED -> RecipeStage.NONE
                    }
                    onStatusChange(status.copy(stage = newStage))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (status.stage) {
                        RecipeStage.NONE -> "Хочу сделать"
                        RecipeStage.WANT_TO_COOK -> "В процессе"
                        RecipeStage.COOKING -> "Приготовлено"
                        RecipeStage.COOKED -> "Приготовлено (нажать для сброса)"
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(recipeId: Int, onBackClick: () -> Unit) {
    val details = getRecipeDetailsById(recipeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(details?.title ?: "Рецепт") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = onBackClick
            ) {
                Text("Обратно к списку")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Сложность: ${details?.difficulty} \n" +
                        "Категория: ${details?.category} \n" +
                        "Калорийность (100g): ${details?.calories} ккал \n" +
                        "Кол-во порций: на ${details?.portions} человек \n" +
                        "Время готовки: ${details?.time}",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ингредиенты:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(
                text = "${details?.ingredients}",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Пошаговое описание:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(
                text = "${details?.description}",
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RecipeApp()
        }
    }
}