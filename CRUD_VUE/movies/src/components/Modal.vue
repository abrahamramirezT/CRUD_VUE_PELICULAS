<template>
  <div>
    <b-button v-b-modal.modal-1>Agregar Película</b-button>
    <b-modal id="modal-1" title="Agregar Película" hide-footer @shown="clearErrors">
      <template #modal-header="{ close }">
        <h5>Agregar Película</h5>
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
  name: "Modal",
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
        .post("http://localhost:8080/api/movies", {
          name: this.name,
          description: this.description,
          category: {
            id: this.selected_category,
          },
        })
        .then((response) => {
          this.name = null;
          this.description = null;
          this.selected_category = null;
          this.errors = [];
          this.$emit("registroExitoso");
          this.$swal({
            title: "Agregada",
            text: "La película ha sido agregada con éxito",
            icon: "success",
          });
          this.$root.$emit("bv::hide::modal", "modal-1");
        })
        .catch((error) => {
          console.error(error);
        });
    },
    closeModal() {
      this.$root.$emit("bv::hide::modal", "modal-1");
      this.name = null;
      this.description = null;
      this.selected_category = null;
    },
    clearErrors() {
      this.errors = [];
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
