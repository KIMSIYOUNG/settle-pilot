import request from "./request";

export const OrderAPI = {
    post: async (data) => {
        const response = await request.post(`/api/orders`, data, {
            withCredentials: true,
        });
        return response;
    },
    reOrder: async (data) => {
        const response = await request.post(`/api/orders/re-order`, data, {
            withCredentials: true,
        });
        return response;
    },
    get: async (id) => {
        const response = await request.get(`/api/orders/${id}`, {
            withCredentials: true,
        });
        return response.data;
    },
    getBySettleId: async (id, page = 0) => {
        const response = await request.get(`/api/orders/settles/${id}`, {
            params: {
                page: page,
                size: 10
            },
            withCredentials: true,
        });
        return response.data;
    },

    getByCondition: async (params, page = 0, size=10) => {
        const response = await request.get(`/api/orders`, {
            params: {
                ...params,
                page: page,
                size: size
            },
            withCredentials: true,
        });
        return response.data;
    },
    patchOrderStatus: async (status) => {
        const response = await request.patch(`/api/orders`, status, {
            withCredentials: true
        })
        return response;
    },
    delete: async (id) => {
        await request.delete(`/api/orders/${id}`, {
            withCredentials: true
        })
    },
}

