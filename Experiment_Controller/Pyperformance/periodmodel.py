#model the periods at which a batch is sent
#inclusive

period = 10
ia = 7
set_upper_bound = 10000

set_p = range(0,set_upper_bound,period)
set_ia = range(0,set_upper_bound,ia)

final_list = []


for p in list(set_p)[1:]:
    num_requests_in_batch = 0
    for sia in set_ia:
        if sia > (p-period) and sia <= p:
            num_requests_in_batch += 1
    final_list.append([p/period,num_requests_in_batch])

#print("list of batch sizes")
#print(final_list)


#analysis

time_between_batches = 0
curr_batch_time = 0
total_num_req = 0
num_batches_with_more_than_one_req = 0

for n in final_list:
    curr_batch_time+=period #update current inter batch
    total_num_req += n[1]
    if n[1]>0:
        #a data transfer has to happen
        time_between_batches+=curr_batch_time #add inter batch time to total
        num_batches_with_more_than_one_req += 1 # add data transfer
        curr_batch_time=0 #reset inter batch time
        
print("-------------------------")
print("average time between batches with at least 1 request")        
#print("total batch time: " + str(time_between_batches))
#print("number of batches sent: " + str(num_batches_with_more_than_one_req))
print(time_between_batches/num_batches_with_more_than_one_req)
print()



print("-------------------------------------")
print("Average batch size")
print("total number of requests: " + str(total_num_req))
print("Average number of requests per batch: "+str(total_num_req/num_batches_with_more_than_one_req))



    
        
        



