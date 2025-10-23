# 📚 Guía: Cómo funciona la integración API → XML en Ciclo Café

## 🎯 Flujo completo de datos

```
API (http://192.168.0.12/api/cafe/products)
    ↓
ProductRepository (hace la llamada HTTP)
    ↓
Product (modelo de datos)
    ↓
EntradaFragment (carga los datos)
    ↓
EntradaItem (representa cada fila)
    ↓
EntradaAdapter (conecta datos con vistas)
    ↓
item_entrada_row.xml (muestra cada producto)
```

---

## 📡 PASO 1: La API y su respuesta

### URL de la API:
```
http://192.168.0.12/api/cafe/products
```

### Respuesta JSON que devuelve:
```json
{
  "success": true,
  "data": [
    {
      "idProducto": "K",
      "name": "CAFETERA",
      "pack": "CAFETERA"
    },
    {
      "idProducto": "P",
      "name": "PEDIDO",
      "pack": "Pedido"
    }
  ],
  "message": "Productos obtenidos exitosamente"
}
```

---

## 📦 PASO 2: Modelo de datos (Product.kt)

**Ubicación:** `app/src/main/java/com/lavazza/ciclocafe/data/model/Product.kt`

```kotlin
package com.lavazza.ciclocafe.data.model

// Este modelo representa CADA producto que devuelve la API
data class Product(
    val idProducto: String,  // "K" o "P"
    val name: String,         // "CAFETERA" o "PEDIDO"
    val pack: String          // "CAFETERA" o "Pedido"
)

// Este modelo representa toda la respuesta de la API
data class ProductResponse(
    val success: Boolean,     // true si la llamada fue exitosa
    val data: List<Product>,  // Lista de productos
    val message: String       // Mensaje de la API
)
```

**¿Por qué es importante?**
- Cada campo del modelo coincide EXACTAMENTE con los campos del JSON
- Kotlin usa estos modelos para convertir automáticamente el JSON en objetos

---

## 🌐 PASO 3: Repositorio (ProductRepository.kt)

**Ubicación:** `app/src/main/java/com/lavazza/ciclocafe/data/repository/ProductRepository.kt`

```kotlin
package com.lavazza.ciclocafe.data.repository

import com.lavazza.ciclocafe.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ProductRepository {
    
    // URL base de la API
    private val baseUrl = "http://192.168.0.12/api/cafe"
    
    // Función suspendida que hace la llamada HTTP
    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            // 1. Crear la conexión HTTP
            val url = URL("$baseUrl/products")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000  // 10 segundos
            connection.readTimeout = 10000     // 10 segundos
            
            // 2. Verificar que la respuesta sea exitosa (código 200)
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                
                // 3. Leer la respuesta como texto
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                
                // 4. Parsear el JSON
                val jsonObject = JSONObject(response)
                
                // 5. Verificar que success sea true
                val success = jsonObject.getBoolean("success")
                if (success) {
                    
                    // 6. Obtener el array "data"
                    val dataArray = jsonObject.getJSONArray("data")
                    val products = mutableListOf<Product>()
                    
                    // 7. Convertir cada elemento del array en un objeto Product
                    for (i in 0 until dataArray.length()) {
                        val productJson = dataArray.getJSONObject(i)
                        products.add(
                            Product(
                                idProducto = productJson.getString("idProducto"),
                                name = productJson.getString("name"),
                                pack = productJson.getString("pack")
                            )
                        )
                    }
                    
                    // 8. Retornar la lista de productos
                    Result.success(products)
                } else {
                    Result.failure(Exception("API returned success=false"))
                }
            } else {
                Result.failure(Exception("HTTP error: $responseCode"))
            }
        } catch (e: Exception) {
            // Si hay cualquier error, retornarlo
            Result.failure(e)
        }
    }
}
```

**¿Qué hace esto?**
1. Se conecta a la API por HTTP
2. Lee la respuesta JSON
3. Convierte el JSON en objetos `Product` de Kotlin
4. Retorna una lista de productos o un error

---

## 🎬 PASO 4: Fragment (EntradaFragment.kt)

**Ubicación:** `app/src/main/java/com/lavazza/ciclocafe/ui/entrada/EntradaFragment.kt`

```kotlin
class EntradaFragment : Fragment() {

    private val repository = ProductRepository()  // Instancia del repositorio
    private var products: List<Product> = emptyList()  // Lista de productos
    private var adapter: EntradaAdapter? = null
    private val entradaItems = mutableListOf<EntradaItem>()

    private fun loadProducts() {
        progressBar?.visibility = View.VISIBLE  // Mostrar loading
        
        // Ejecutar en una corrutina (hilo secundario)
        lifecycleScope.launch {
            repository.getProducts().fold(
                onSuccess = { productList ->
                    // ✅ SI LA API RESPONDE BIEN:
                    products = productList  // Guardar productos
                    progressBar?.visibility = View.GONE  // Ocultar loading
                    
                    // Crear un EntradaItem por cada producto
                    entradaItems.clear()
                    for (product in products) {
                        entradaItems.add(
                            EntradaItem(
                                producto = product.name,  // "CAFETERA" o "PEDIDO"
                                selectedProduct = product,
                                vueltaLleno = 0,
                                vueltaTotal = 0
                            )
                        )
                    }
                    
                    // Pasar los datos al adaptador
                    adapter = EntradaAdapter(products)
                    recyclerView.adapter = adapter
                    adapter?.submitList(entradaItems.toList())  // ← AQUÍ SE ENVÍAN AL XML
                },
                onFailure = { error ->
                    // ❌ SI HAY ERROR:
                    progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    
                    // Usar productos de respaldo
                    products = listOf(
                        Product("K", "CAFETERA", "CAFETERA"),
                        Product("P", "PEDIDO", "Pedido")
                    )
                    // ... crear items y mostrar ...
                }
            )
        }
    }
}
```

**¿Qué hace esto?**
1. Llama a `repository.getProducts()`
2. Por cada producto recibido, crea un `EntradaItem`
3. Pasa la lista al `EntradaAdapter`
4. El adapter se encarga de mostrar cada item en el XML

---

## 🔗 PASO 5: Adaptador (EntradaAdapter.kt)

**Ubicación:** `app/src/main/java/com/lavazza/ciclocafe/ui/entrada/EntradaAdapter.kt`

```kotlin
class EntradaAdapter(
    private val products: List<Product>  // Lista de productos de la API
) : ListAdapter<EntradaItem, EntradaAdapter.EntradaViewHolder>(EntradaDiffCallback()) {

    // Este método se llama para crear cada fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntradaViewHolder {
        // Inflar el XML item_entrada_row.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrada_row, parent, false)
        return EntradaViewHolder(view)
    }

    // Este método se llama para mostrar los datos de cada item
    override fun onBindViewHolder(holder: EntradaViewHolder, position: Int) {
        holder.bind(getItem(position))  // Pasar el item a la vista
    }

    class EntradaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Conectar con los elementos del XML
        private val textProducto: TextView = itemView.findViewById(R.id.textProducto)
        private val editVueltaLleno: EditText = itemView.findViewById(R.id.editVueltaLleno)
        private val editVueltaTotal: EditText = itemView.findViewById(R.id.editVueltaTotal)
        private val textCambio: TextView = itemView.findViewById(R.id.textCambio)

        fun bind(item: EntradaItem) {
            // ← AQUÍ SE ASIGNAN LOS VALORES AL XML
            
            // Mostrar el nombre del producto
            textProducto.text = item.producto  // "CAFETERA" o "PEDIDO"
            
            // Mostrar las cantidades
            editVueltaLleno.setText(if (item.vueltaLleno == 0) "" else item.vueltaLleno.toString())
            editVueltaTotal.setText(if (item.vueltaTotal == 0) "" else item.vueltaTotal.toString())
            
            // Mostrar el cambio calculado
            updateCambio()
            
            // Listener para cuando cambie "Vuelta lleno"
            editVueltaLleno.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.vueltaLleno = s.toString().toIntOrNull() ?: 0
                    updateCambio()  // Recalcular cambio
                }
                // ...
            })
            
            // Listener para cuando cambie "Vuelta total"
            editVueltaTotal.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.vueltaTotal = s.toString().toIntOrNull() ?: 0
                    updateCambio()  // Recalcular cambio
                }
                // ...
            })
        }

        private fun updateCambio() {
            currentItem?.let {
                // Cambio = Vuelta total - Vuelta lleno
                textCambio.text = it.cambio.toString()
            }
        }
    }
}
```

**¿Qué hace esto?**
1. Recibe la lista de `EntradaItem`
2. Por cada item, crea una fila usando `item_entrada_row.xml`
3. Conecta los datos con los elementos del XML usando `findViewById`
4. Asigna los valores: `textProducto.text = item.producto`

---

## 📱 PASO 6: Layout XML (item_entrada_row.xml)

**Ubicación:** `app/src/main/res/layout/item_entrada_row.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="8dp">

    <!-- Nombre del producto (viene de la API) -->
    <TextView
        android:id="@+id/textProducto"
        android:layout_width="0dp"
        android:layout_weight="2"
        android:textStyle="bold"
        android:gravity="center" />
        ↑
        Este TextView muestra "CAFETERA" o "PEDIDO"

    <!-- Campo para ingresar Vuelta lleno -->
    <EditText
        android:id="@+id/editVueltaLleno"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:hint="0"
        android:inputType="number"
        android:gravity="center" />
        ↑
        El usuario ingresa un número aquí

    <!-- Campo para ingresar Vuelta total -->
    <EditText
        android:id="@+id/editVueltaTotal"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:hint="0"
        android:inputType="number"
        android:gravity="center" />
        ↑
        El usuario ingresa un número aquí

    <!-- Cambio calculado automáticamente -->
    <TextView
        android:id="@+id/textCambio"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:text="0"
        android:gravity="center" />
        ↑
        Muestra el resultado: Vuelta total - Vuelta lleno

</LinearLayout>
```

**¿Cómo se conecta?**
- Cada elemento tiene un `android:id` único
- El adapter usa `findViewById(R.id.textProducto)` para obtener la referencia
- Luego asigna el valor: `textProducto.text = "CAFETERA"`

---

## 🔄 RESUMEN: Flujo completo con ejemplo

### Ejemplo: API devuelve 2 productos

```
1. API responde:
   {
     "data": [
       {"idProducto": "K", "name": "CAFETERA", "pack": "CAFETERA"},
       {"idProducto": "P", "name": "PEDIDO", "pack": "Pedido"}
     ]
   }

2. ProductRepository convierte JSON → List<Product>:
   [
     Product("K", "CAFETERA", "CAFETERA"),
     Product("P", "PEDIDO", "Pedido")
   ]

3. EntradaFragment crea EntradaItem por cada producto:
   [
     EntradaItem(producto="CAFETERA", vueltaLleno=0, vueltaTotal=0),
     EntradaItem(producto="PEDIDO", vueltaLleno=0, vueltaTotal=0)
   ]

4. EntradaAdapter recibe la lista y crea 2 filas:
   
   Fila 1:
   ┌────────────┬──────────┬──────────┬─────────┐
   │ CAFETERA   │   [ ]    │   [ ]    │    0    │
   └────────────┴──────────┴──────────┴─────────┘
   
   Fila 2:
   ┌────────────┬──────────┬──────────┬─────────┐
   │ PEDIDO     │   [ ]    │   [ ]    │    0    │
   └────────────┴──────────┴──────────┴─────────┘

5. Usuario ingresa datos:
   - CAFETERA: Vuelta lleno = 5, Vuelta total = 8
   - PEDIDO: Vuelta lleno = 3, Vuelta total = 10

6. El adapter calcula automáticamente:
   - CAFETERA: Cambio = 8 - 5 = 3
   - PEDIDO: Cambio = 10 - 3 = 7

7. Resultado final:
   ┌────────────┬──────────┬──────────┬─────────┐
   │ CAFETERA   │    5     │    8     │    3    │
   │ PEDIDO     │    3     │    10    │    7    │
   └────────────┴──────────┴──────────┴─────────┘
```

---

## 🔑 Conceptos clave

### 1. **Modelo de datos (Product)**
- Define la estructura de cada producto
- Cada campo coincide con el JSON de la API

### 2. **Repository (ProductRepository)**
- Hace la llamada HTTP a la API
- Parsea el JSON y crea objetos Product
- Maneja errores de conexión

### 3. **Item (EntradaItem)**
- Representa una fila en la tabla
- Incluye el producto + las cantidades ingresadas
- Calcula el cambio automáticamente

### 4. **Adapter (EntradaAdapter)**
- Es el "puente" entre los datos y el XML
- Por cada item, crea una fila
- Conecta los datos con los elementos del XML

### 5. **Layout XML (item_entrada_row.xml)**
- Define CÓMO se ve cada fila
- Los elementos tienen IDs para poder accederlos desde Kotlin

---

## 📝 Para agregar más campos de la API

Si la API devuelve más información, solo necesitas:

1. **Actualizar el modelo Product.kt:**
```kotlin
data class Product(
    val idProducto: String,
    val name: String,
    val pack: String,
    val precio: Double  // ← Nuevo campo
)
```

2. **Actualizar el Repository:**
```kotlin
Product(
    idProducto = productJson.getString("idProducto"),
    name = productJson.getString("name"),
    pack = productJson.getString("pack"),
    precio = productJson.getDouble("precio")  // ← Parsear nuevo campo
)
```

3. **Agregar al XML si quieres mostrarlo:**
```xml
<TextView
    android:id="@+id/textPrecio"
    android:layout_width="0dp"
    android:layout_weight="1" />
```

4. **Mostrar en el Adapter:**
```kotlin
val textPrecio: TextView = itemView.findViewById(R.id.textPrecio)
textPrecio.text = "$${item.selectedProduct?.precio}"
```

---

## 🎯 Conclusión

El flujo es:
```
API → Repository → Fragment → Adapter → XML
```

Cada capa tiene una responsabilidad específica:
- **API:** Provee los datos
- **Repository:** Obtiene y parsea los datos
- **Fragment:** Coordina la carga de datos
- **Adapter:** Conecta datos con vistas
- **XML:** Define la apariencia

¡Así funciona la integración API → XML en tu app! 🚀

