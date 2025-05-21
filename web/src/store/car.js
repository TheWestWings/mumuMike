const carModule = {

    namespaced: true,

    state: {
        carList: localStorage.getItem('carList') ? JSON.parse(localStorage.getItem('carList')) : [],
    },

    getters: {

    },

    mutations: {
        setCarList(state, carList) {
            state.carList = carList;
            localStorage.setItem('carList', JSON.stringify(carList));
        },
        clearCarList(state) {
            state.carList = [];
            localStorage.removeItem('carList');
        },
    },
};

export default carModule;