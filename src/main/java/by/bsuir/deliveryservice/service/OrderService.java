/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;

import javax.xml.ws.Service;
import java.util.List;

public interface OrderService {
    Long placeOrder(Order order) throws ServiceException;
    void updateOrderStatus(Long orderId, OrderStatus status) throws ServiceException;
    void assignCourier(Long orderId, Long courierId) throws ServiceException;
    Double calculatePrice(Double weight, Double distance, Long shippingId) throws ServiceException;

    void declineOrder(Long orderId) throws ServiceException;
    void deliverOrder(Long orderId) throws ServiceException;
    void cancelOrder(Long orderId) throws ServiceException;
    void approveOrder(Long orderId) throws ServiceException;

    Order viewOrder(Long orderId) throws ServiceException;
    List<Order> viewUserApplications(Long userId) throws ServiceException;
    List<Order> viewApplications() throws ServiceException;
    List<Order> viewCourierOrders(Long courierId) throws ServiceException;
    List<Order> viewOrders() throws ServiceException;
    List<Order> viewUserOrders(Long userId) throws ServiceException;

    int selectCountCourierOrders(Long courierId) throws ServiceException;
}
