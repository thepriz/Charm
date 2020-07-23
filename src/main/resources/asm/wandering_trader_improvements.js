function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var IntInsnNode = Java.type('org.objectweb.asm.tree.IntInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {
        'wandering_trader_random_chance': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.world.spawner.WanderingTraderSpawner',
                'methodName': 'func_234562_a_', // not mapped in 1.16
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;)Z'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.BIPUSH) {
                        newInstructions.add(new IntInsnNode(Opcodes.BIPUSH, 1));
                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (WanderingTraderImprovements) " + (success ? "Patched WanderingTraderSpawner (random chance)" : "Failed to patch WanderingTraderSpawner (random chance)"));
                return method;
            }
        },
        'wandering_trader_signal_fire_hook': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.world.spawner.WanderingTraderSpawner',
                'methodName': 'func_234562_a_', // not mapped in 1.16
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;)Z'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.ASTORE
                        && instruction.var == 3
                    ) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "isSignalFireInRange", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", ASM.MethodType.STATIC));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFNE, label));
                        newInstructions.add(new InsnNode(Opcodes.ICONST_0));
                        newInstructions.add(new InsnNode(Opcodes.IRETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (WanderingTraderImprovements) " + (success ? "Patched WanderingTraderSpawner (signal fire hook)" : "Failed to patch WanderingTraderSpawner (signal fire hook)"));
                return method;
            }
        }
    }
}