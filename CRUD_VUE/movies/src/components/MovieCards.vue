<template>
  <div class="container">
    <div class="header">
      <h1>Películas</h1>
      <Modal @registroExitoso="actualizarPeliculas"></Modal>
    </div>

    <div class="loader" v-if="loading">
      <div class="spinner-border text-primary" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-lg-3 col-md-4 col-sm-6 mb-4" v-for="pelicula in peliculas" :key="pelicula.id">
        <div class="card h-100">
          <img class="card-img-top" src="https://imgs.search.brave.com/89dFik2P4iqSs6AiAmlnNtA7107Jy0DNp9Hd6mzAihg/rs:fit:860:0:0/g:ce/aHR0cHM6Ly9pLmd1/aW0uY28udWsvaW1n/L21lZGlhLzFmYWY2/OTkzNzEyYmY5YzE0/MzZhYzQ1ZmVmOWNl/N2VhNjQxM2Y0ZWEv/MF80MDBfNjAwMF8z/NjAyL21hc3Rlci82/MDAwLmpwZz93aWR0/aD00NjUmZHByPTEm/cz1ub25l" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">{{ pelicula.name }}</h5>
            <p class="card-text">{{ pelicula.description }}</p>
            <p class="badge badge-dark">{{ pelicula.category.name }}</p>
          </div>
          <div class="card-footer">
            <div class="btn-group d-flex" role="group">
              <button type="button" class="btn btn-danger flex-fill" @click="deleteMovie(pelicula.id)">Eliminar</button>
              <EditModal :pelicula="pelicula" @actualizacionExitosa="actualizarPeliculas"></EditModal>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "./Modal.vue";
import EditModal from "./EditModal.vue";
import axios from "axios";
export default {
  name: "MovieCards",
  components: {
    Modal,
    EditModal,
  },
  data() {
    return {
      peliculas: [],
      loading: null,
    };
  },
  methods: {
    actualizarPeliculas() {
      this.loading = true;
      axios
        .get("http://localhost:8080/api/movies")
        .then((response) => {
          this.peliculas = response.data;
        })
        .catch((e) => {
          console.error("Error en la petición: ", e);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    deleteMovie(id) {
      this.$swal({
        title: "¿Estás seguro?",
        text: "No podrás revertir este cambio",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        cancelButtonText: "Cancelar",
        confirmButtonText: "Sí, eliminar",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .delete(`http://localhost:8080/api/movies/${id}`)
            .then((response) => {
              this.$swal({
                title: "Eliminada",
                text: "La película ha sido eliminada con éxito",
                icon: "success",
              });
              this.actualizarPeliculas();
            })
            .catch((error) => {
              console.error(error);
            });
        }
      });
    },
  },
  mounted() {
    this.actualizarPeliculas();
  },
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.loader {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

.card {
  width: 18rem;
}

.btn {
  width: 100%;
}

@media (max-width: 768px) {
  .card {
    width: 100%;
  }
}
</style>
