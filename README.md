# Algoritmo Goloso para el Problema de Clique de Peso Máximo

## Descripción
El trabajo práctico consiste en implementar un algoritmo goloso para resolver el problema de encontrar una clique de peso máximo en un grafo dado.

### Definición de Clique
Una *clique* es un conjunto de vértices tal que todos los vértices del conjunto son vecinos entre sí. Se asume que cada vértice tiene un peso asociado, y el peso de una clique es la suma de los pesos de sus vértices.

### Objetivo
El objetivo es hallar una clique que tenga el peso total máximo.

[!Figura 1](https://imgur.com/a/s9y5LP8)
*Figura 1: La clique de peso máximo en este grafo es {1, 2, 4}, y tiene un peso de 23.5.*

## Implementación
Se debe desarrollar una aplicación que utilice un algoritmo goloso para encontrar la clique de mayor peso posible a partir de los datos del grafo proporcionados.

### Métodos de Ingreso de Datos
1. **Interfaz Manual:** Permitir al usuario cargar el grafo manualmente, con opciones para agregar vértices y arcos. Al agregar un vértice, se solicita el peso al usuario. Al agregar un arco, se solicitan los dos extremos del mismo.
2. **Lectura desde Archivo:** Leer el grafo desde un archivo en el formato definido por el grupo (texto plano o JSON).

### Objetivos Opcionales
- Incorporar un elemento aleatorio al algoritmo para obtener resultados distintos en cada ejecución.
- Proporcionar estadísticas de la ejecución, como tiempo total y nodos evaluados.
- Visualizar el grafo y la clique resultante en la interfaz de usuario.

## Entrega
- **Formato:** El trabajo se entrega por correo electrónico a los docentes de la materia.
- **Documentación:** Incluir un documento descriptivo de la implementación y las decisiones de desarrollo.
- **Tests Unitarios:** Todas las clases de negocio deben tener un conjunto adecuado de tests unitarios.
- **Grupos:** El trabajo se puede realizar en grupos de tres o cuatro personas.

## Fecha de Entrega
**Martes 11 de junio**
