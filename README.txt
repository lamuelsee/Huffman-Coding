Samuel Lee
slee248@u.rochester.edu
CSC 172 Project 2, University of Rochester
I worked on this project alone

In this project, I implemented Huffman encoding and decoding.
This was done utilizing nodes, priority queues and dictionaries.

The encoding was done by finding the frequency of every character 
in the file. These values were placed both into a dictionary, as
well as into the frequency file (which will be utilized in the 
decoding part). Then, iterating through this frequency dictionary, 
the characters and their frequencies are placed into a node, which
is subsequently put into a priority queue. After all the characters
have been accounted for, the huffman tree is created utilizing this 
priority queue. This huffman tree is then used to create the huffman
values, which are all, once again, placed into a dictionary. This 
dictionary then utilized alongside the original file to create a new
encoded file, which holds the encoded version of the text. 

The decoding file utilizes the frequency file created by the encoding
function. Iterating through each line, it places the characters and 
its frequencies into a node. These nodes are all then placed into a 
priority queue, which is used to create a huffman tree. Then, the huffman
values of each character are all determined (once again), and this is used
to decode the encrypted file.

There is one issue that arose from my Huffman implementation. The encoding
of the file seems to work perfectly fine; however, when decoding the 
encrypted file, there sometimes is extra "noise" added. What I mean is that,
for example, for the Alice file, an extra space is added at the end of 
the file. I tried numerous patches to try and find the solution (flushing
more often, checking implementation, tracing); however, I was not able to 
find what the issue was caused by, and thus, was not able to fix 
this bug. I also consulted friends who have previously done this project,
and it seems that this is a common issue. Given that I had more time
(and help too maybe), I think that the cause of this bug could have 
been found, but I am submitting as is due to the time constraints. 