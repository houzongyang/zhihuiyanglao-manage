<template>
  <div id="app">
    <div v-if="isBeta" class="beta-badge">Beta</div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    isBeta() {
      return process.env.VUE_APP_ENV === 'beta'
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
}
.beta-badge {
  position: fixed;
  top: 8px;
  right: 8px;
  background: #ff4d4f;
  color: #fff;
  font-weight: bold;
  padding: 4px 8px;
  border-radius: 4px;
  z-index: 9999;
  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
}
</style>