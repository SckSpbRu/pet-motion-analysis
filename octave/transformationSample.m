# v3d - coordinates in real world 
v3d = [42 77 90 160 
162 220 314 288
0 0 0 0
1 1 1 1]

# v2d - coordinates on a 2d picture 
v2d = [286 244 98 239 
80 271 428 555
0 0 0 0
1 1 1 1]

# Tr * v3d = v2d 
# Tr = v2d * v3d ^ -1
Tr = v2d * pinv(v3d)

# testing that it works 
v2d_reconstructed_with_Tr = Tr * v3d 

# testing backwards
# v3d = Tr^-1 * v2d  
v3d_reconstructued_with_Tr = pinv(Tr) * v2d 