<template>
  <div>
    <b-button
      style="width: 100%; padding: 8px"
      v-b-modal="`modalEdicion_${pelicula.id}`"
      @click="fillForm"
    >
      Editar
    </b-button>
    <b-modal
      :id="`modalEdicion_${pelicula.id}`"
      title="Editar Película"
      hide-footer
    >
      <template #modal-header="{ close }">
        <h5>Editar Película</h5>
        <b-button size="sm" variant="outline-danger" @click="close()">X</b-button>
      </template>
      <form id="form" @submit.prevent="checkForm">
        <div v-if="errors.length">
          <b-alert show variant="danger" dismissible fade>
            <strong>Por favor, corrija el(los) siguiente(s) error(es):</strong>
            <ul class="mb-0">
              <li v-for="error in errors" :key="error">{{ error }}</li>
            </ul>
          </b-alert>
        </div>
        <div class="form-group">
          <label for="name">Nombre:</label>
          <input id="name" v-model="name" type="text" name="name" class="form-control" required>
        </div>
        <div class="form-group">
          <label for="description">Descripción:</label>
          <textarea id="description" v-model="description" name="description" rows="3" class="form-control" required></textarea>
        </div>
        <div class="form-group">
          <label for="category">Categoría:</label>
          <select id="category" v-model="selected_category" name="category" class="form-control" required>
            <option disabled value="">Seleccionar categoría</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
          </select>
        </div>
        <div class="buttons-container">
          <button type="submit" class="btn btn-primary">Enviar</button>
          <button type="button" @click="closeModal" class="btn btn-secondary">Cancelar</button>
        </div>
      </form>
    </b-modal>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "EditModal",
  props: {
    pelicula: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      errors: [],
      name: null,
      description: null,
      selected_category: null,
      categories: [],
    };
  },
  methods: {
    fillForm() {
      this.name = this.pelicula.name;
      this.description = this.pelicula.description;
      this.selected_category = this.pelicula.category.id;
    },
    checkForm() {
      if (this.name && this.description && this.selected_category) {
        this.sendForm();
        return true;
      }
      this.errors = [];
      if (!this.name) {
        this.errors.push("El nombre es obligatorio.");
      }
      if (!this.description) {
        this.errors.push("La descripción es obligatoria.");
      }
      if (!this.selected_category) {
        this.errors.push("Debes seleccionar una categoría.");
      }
    },
    sendForm() {
      axios
        .patch(`http://localhost:8080/api/movies/${this.pelicula.id}`, {
          name: this.name,
          description: this.description,
          category: {
            id: this.selected_category,
          },
        })
        .then((response) => {
          console.log(response);
          this.$emit("actualizacionExitosa");
          this.$swal({
            title: "Actualizada",
            text: "La película ha sido actualizada con éxito",
            icon: "success",
          });
          this.$root.$emit(
            "bv::hide::modal",
            `modalEdicion_${this.pelicula.id}`
          );
        })
        .catch((error) => {
          console.error(error);
        });
    },
    closeModal() {
      this.$root.$emit("bv::hide::modal", `modalEdicion_${this.pelicula.id}`);
    },
  },
  mounted() {
    axios
      .get("http://localhost:8080/api/categories")
      .then((response) => {
        this.categories = response.data;
      })
      .catch((e) => {
        console.error("Error en la petición: ", e);
      });
  },
};
</script>

<style scoped>
.buttons-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.alert {
  margin-bottom: 20px;
}
</style>
